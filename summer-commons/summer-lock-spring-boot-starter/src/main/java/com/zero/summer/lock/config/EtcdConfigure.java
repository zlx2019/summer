package com.zero.summer.lock.config;

import io.etcd.jetcd.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Etcd Client配置类
 * @author Zero.
 * @date 2023/2/13 8:52 PM
 */
@Configuration
@Slf4j
public class EtcdConfigure {

    /**
     * 初始化Etcd的客户端
     * @return {@link Client}
     */
    @Bean
    public Client client(){
        return Client.builder().endpoints("http://localhost:2379").build();
    }
}
