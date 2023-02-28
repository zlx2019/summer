package com.zero.summer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 @RSocketExchange
 * @author Zero.
 * @date 2023/2/2 9:00 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class,args);
    }
}
