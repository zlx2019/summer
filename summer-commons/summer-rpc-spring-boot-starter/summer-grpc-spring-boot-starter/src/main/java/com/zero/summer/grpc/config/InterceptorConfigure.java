package com.zero.summer.grpc.config;

import com.zero.summer.grpc.Interceptor.DefaultGrpcServerInterceptor;
import io.grpc.ServerInterceptor;
import org.lognet.springboot.grpc.GRpcGlobalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * gRPC 全局拦截器配置
 *
 * @author Zero.
 * @date 2023/3/5 3:40 PM
 */
@Configuration
public class InterceptorConfigure {


    /**
     * 注入服务端全局拦截器
     * @return {@link DefaultGrpcServerInterceptor}
     */
    @Bean
    @GRpcGlobalInterceptor
    public ServerInterceptor serverInterceptor(){
        return new DefaultGrpcServerInterceptor();
    }
}
