package com.zero.summer.grpc.client;

import com.zero.summer.core.constant.ServiceConst;
import com.zero.summer.core.constant.ServiceMetadataConst;
import com.zero.summer.grpc.Interceptor.DefaultGrpcClientInterceptor;
import com.zero.summer.grpc.pb.example.ExampleReply;
import com.zero.summer.grpc.pb.example.ExampleRequest;
import com.zero.summer.grpc.pb.example.ExampleServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Component;

/**
 * Example gRPC服务的客户端
 * @author Zero.
 * @date 2023/3/5 12:38 AM
 */
@Component
@Slf4j
public class ExampleClient {

    /**
     * 调用Example服务的客户端
     */
    private ExampleServiceGrpc.ExampleServiceBlockingStub stub;
    /**
     * 用来获取Example服务的节点信息
     */
    @Autowired
    private LoadBalancerClient loadBalancer;


    /**
     * 测试gRPC 接口
     * @param message 参数消息
     * @return        响应消息
     */
    public String example(String message){
        if (stub == null){
            // 如果客户端未注册,则进行注册.
            registryService();
        }
        ExampleRequest request = ExampleRequest.newBuilder().setMessage(message).build();
        ExampleReply reply = stub.exampleTest(request);
        return reply.getResult();
    }

    /**
     * Bean初始化后
     */
    @PostConstruct
    public void init(){
        // 初始化后优先注入一次,避免后续阻塞,影响性能.
        registryService();
    }

    /**
     * 获取服务信息,注册到gRPC客户端中.
     */
    private synchronized void registryService(){
        // 获取服务信息
        ServiceInstance instance = loadBalancer.choose(ServiceConst.EXAMPLE);
        if (instance == null){
            return;
        }
        // 获取服务的gRPC端口
        String grpcPort = instance.getMetadata().get(ServiceMetadataConst.GRPC_PORT);
        // 注册到客户端中
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(instance.getHost(), Integer.parseInt(grpcPort))
                // 设置客户端拦截器
                .intercept(new DefaultGrpcClientInterceptor())
                .usePlaintext().build();
        stub = ExampleServiceGrpc.newBlockingStub(channel);
    }
}
