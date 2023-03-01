package com.zero.summer.gateway.config;

import com.zero.summer.gateway.filter.TraceFilter;
import com.zero.summer.gateway.handler.GatewayExceptionHandler;
import com.zero.summer.gateway.handler.SentinelBlockRequestHandler;
import com.zero.summer.gateway.rule.SentinelDegradeRule;
import com.zero.summer.gateway.rule.SentinelLimitingRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 网关全局配置类
 *
 * @author Zero.
 * @date 2023/3/1 10:09 PM
 */
@Configuration
@Slf4j
public class GatewayConfigurer {

    /**
     * 注入网关统一异常处理
     * @return {@link GatewayExceptionHandler}
     */
    @Bean
    public GatewayExceptionHandler gatewayExceptionHandler(){
        return new GatewayExceptionHandler();
    }

    /**
     * 注入日志链路追踪ID生成过滤器
     * @return {@link TraceFilter}
     */
    @Bean
    public TraceFilter traceFilter(){
        return new TraceFilter();
    }

    /**
     * 注入Sentinel 限流异常处理
     * @return {@link SentinelBlockRequestHandler}
     */
    @Bean
    public SentinelBlockRequestHandler sentinelBlockRequestHandler(){
        return new SentinelBlockRequestHandler();
    }

    /**
     * 注入自定义Sentinel限流规则
     * @return  {@link SentinelLimitingRule}
     */
    @Bean
    public SentinelLimitingRule sentinelLimitingRule(){
        return new SentinelLimitingRule();
    }

    /**
     * 注入自定义Sentinel降级规则
     * @return {@link SentinelDegradeRule}
     */
    @Bean
    public SentinelDegradeRule sentinelDegradeRule(){
        return new SentinelDegradeRule();
    }
}
