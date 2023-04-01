package com.zero.summer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;

/**
 @RSocketExchange
 * @author Zero.
 * @date 2023/2/2 9:00 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ExampleApplication  {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class,args);
    }

    @PostConstruct
    public void init(){
        String[] names = context.getBeanNamesForType(ApplicationContext.class);
        System.out.println("context size:" + names.length);
    }
}
