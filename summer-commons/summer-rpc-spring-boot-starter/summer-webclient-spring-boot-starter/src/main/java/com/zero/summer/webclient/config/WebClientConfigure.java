package com.zero.summer.webclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * WebClient注入
 *
 * @author Zero.
 * @date 2023/2/14 4:26 PM
 */
@Slf4j
public class WebClientConfigure {

    /**
     * 复杂均衡过滤器.
     * 让WebClient支持根据服务名调用,并且采用负载均衡算法。
     */
    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction loadBalancerFilter;

    /**
     * 注入WebClient 构建器
     * @return  {@link WebClient.Builder}
     */
    @Bean
    public WebClient.Builder builder(){
        return WebClient.builder().filter(loadBalancerFilter);
    }

    /**
     * 注入为声明式接口创建代理对象的工厂构建器
     * @return {@link HttpServiceProxyFactory}
     */
    @Bean
    public HttpServiceProxyFactory.Builder factoryBuild(){
        return HttpServiceProxyFactory.builder();
    }
}
