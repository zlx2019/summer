package com.zero.summer;

import com.zero.summer.log.monitor.PointUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Zero.
 * @date 2023/2/14 1:46 PM
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(UserApplication.class, args);
        PointUtil.info("1001","CCC","HHHH");
//        RSocketMessageHandler handler = context.getBean(RSocketMessageHandler.class);
//        CloseableChannel server = RSocketServer.create(handler.responder())
//                .bind(TcpServerTransport.create("localhost", 7000))
//                .doOnSuccess(i-> {
//                    System.out.println("启动RSocket成功");
//                })
//                .block();
    }
}
