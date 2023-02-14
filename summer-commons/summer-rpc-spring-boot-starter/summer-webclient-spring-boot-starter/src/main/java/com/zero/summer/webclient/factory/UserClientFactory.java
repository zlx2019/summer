package com.zero.summer.webclient.factory;

import com.zero.summer.webclient.client.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.time.Duration;

/**
 * 所有用户服务相关的声明式接口的注入工厂
 *
 * @author Zero.
 * @date 2023/2/14 4:36 PM
 */
@Slf4j
public class UserClientFactory {


    /**
     * 注入{@link UserServiceClient}声明式接口
     * 只有通过{@link HttpServiceProxyFactory}工厂使用WebClient为接口创建代理对象后,才可以进行服务调用.
     *
     * @return {@link UserServiceClient}的代理对象
     */
    @Bean
    public UserServiceClient userCallService(WebClient.Builder builder){
        return HttpServiceProxyFactory
                .builder(WebClientAdapter.forClient(builder.build()))
                .blockTimeout(Duration.ofSeconds(10)) // 调用超时时间
                .build().createClient(UserServiceClient.class);//创建接口的代理对象
    }

}
