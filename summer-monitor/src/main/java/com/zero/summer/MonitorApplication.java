package com.zero.summer;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Admin 监控服务启动类
 *
 * @author Zero.
 * @date 2023/4/1 1:44 PM
 */
@EnableAdminServer
@SpringBootApplication
@EnableDiscoveryClient
public class MonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class,args);
    }
}
