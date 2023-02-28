package com.zero.summer.webclient.config;

import com.zero.summer.webclient.filter.GlobalWebClientFilter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;

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
     * maxConnections(): 指定了可以一次性创建的最大连接数。如果ConnectionProvider收到的连接请求超过了maxConnections，则将拒绝新的连接请求，直到有连接被关闭。
     * pendingAcquireTimeout(): 用于控制ConnectionProvider在等待获取连接的最长时间，以毫秒为单位。如果超过此时间，则ConnectionProvider将抛出TimeoutException。
     * pendingAcquireMaxCount(): 允许挂起(等待)的最大请求数，当挂起的请求数超过该值时，ConnectionProvider会抛出TooManyPendingAcquireException异常。
     *
     * 例如如下配置: 最多创建48个连接。超过48个连接后将请求挂起等待有连接释放,最大挂起96个请求,超过后抛出异常。如果挂起的时间超过15s,则抛出异常。
     */
    @Bean
    public HttpClient webClientHttp(){
        // 自定义连接池管理
        ConnectionProvider provider = ConnectionProvider
                .builder("web-client-task")//池中的线程名
                .maxConnections(48)//连接池允许连接的最大连接数
                .pendingAcquireTimeout(Duration.ofSeconds(50))//等待连接超时时间
                .pendingAcquireMaxCount(96) //
                .metrics(false).build();
        // 定义一个loop来进行http的线程调度管理
        LoopResources loopResources = LoopResources.create("web-client-loop");
        // 定义HttpClient
        HttpClient httpClient = HttpClient.create(provider)
                // 设置请求连接超时时间
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
                .runOn(loopResources)
                .doOnConnected(conn -> {
                    // 设置读取数据超时时间 15s
                    conn.addHandlerLast(new ReadTimeoutHandler(15));
                    // 设置写入数据超时时间 15s
                    conn.addHandlerLast(new WriteTimeoutHandler(15));
                });
        return httpClient;
    }

    /**
     * 注入WebClient 构建器
     * @return  {@link WebClient.Builder}
     */
    @Bean
    public WebClient.Builder builder(HttpClient webClientHttp){
        return WebClient.builder()
                // 设置全局请求头
                .defaultHeaders(headers->{
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.setAccept(List.of(MediaType.ALL));
                })
                // 设置编码解码器最大缓存,默认:256kb。-1:不限制
                .codecs(conf->{
                    conf.defaultCodecs().maxInMemorySize(-1);
                })
                .clientConnector(new ReactorClientHttpConnector(webClientHttp))
                // 设置全局过滤器
                .filters(list->{
                    list.add(loadBalancerFilter);//负载均衡过滤器
                    list.add(tokenFilter());//数据传递过滤器
                });
    }
}
