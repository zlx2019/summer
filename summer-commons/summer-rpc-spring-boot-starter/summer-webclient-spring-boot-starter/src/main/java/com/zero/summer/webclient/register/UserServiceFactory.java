package com.zero.summer.webclient.register;

import com.zero.summer.core.constant.ServiceConst;
import com.zero.summer.webclient.service.UserCallService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserServiceFactory {

    /**
     * WebClient的构建器
     */
    @Autowired
    private WebClient.Builder builder;
    @Autowired
    private HttpServiceProxyFactory.Builder factoryBuild;

    /**
     * 创建User服务的WebClient
     *
     * @return {@link WebClient}
     */
    @Bean
    public WebClient userServiceClient(){
        return builder.baseUrl("http://" + ServiceConst.USER).build();
    }

    /**
     * 注入{@link UserCallService}声明式接口
     * 只有通过{@link HttpServiceProxyFactory}工厂使用WebClient为接口创建代理对象后,才可以进行服务调用.
     *
     * @return {@link UserCallService}的代理对象
     */
    @Bean
    public UserCallService userCallService(@Autowired WebClient userServiceClient){
        HttpServiceProxyFactory factory = factoryBuild
                .clientAdapter(WebClientAdapter.forClient(userServiceClient))
                .blockTimeout(Duration.ofSeconds(5)).build();
        return factory.createClient(UserCallService.class);
    }

}
