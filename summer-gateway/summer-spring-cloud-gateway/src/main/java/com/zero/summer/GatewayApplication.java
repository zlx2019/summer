package com.zero.summer;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Summer 网关服务
 *
 * @author Zero.
 * @date 2023/3/1 9:31 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class GatewayApplication {
    public static void main(String[] args) {
        System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY,"1");
        SpringApplication.run(GatewayApplication.class,args);
    }
}
