package com.zero.summer.webclient.config;

import com.zero.summer.webclient.filter.GlobalWebClientFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.List;

/**
 * 全局WebClient配置
 *
 * @author Zero.
 * @date 2023/2/14 4:26 PM
 */
@Slf4j
public class WebClientConfigure {

    /**
     * 负载均衡过滤器.
     * 让WebClient支持根据服务名调用,并且采用负载均衡算法。
     */
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction loadBalancerFilter;

    /**
     * 注入WebClient全局过滤器,实现上下游服务数据传递
     * @return  {@link GlobalWebClientFilter}
     */
    @Bean
    public ExchangeFilterFunction tokenFilter(){
        return new GlobalWebClientFilter();
    }

    /**
     * 注入WebClient 构建器
     * @return  {@link WebClient.Builder}
     */
    @Bean
    public WebClient.Builder builder(){
        return WebClient.builder()
                // 设置默认请求头
                .defaultHeaders(headers->{
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(List.of(MediaType.ALL));
                })
                .filter(loadBalancerFilter) //负载均衡过滤器
                .filter(tokenFilter());     //数据传递过滤器
    }

    public HttpClient httpClient(){
        // 自定义管理连接
        ConnectionProvider provider = ConnectionProvider
                .builder("web-client-task")//name
                .maxConnections(3000)//最大连接数
                .pendingAcquireTimeout(Duration.ofSeconds(10)).metrics(false).build();
        return null;
    }
}
