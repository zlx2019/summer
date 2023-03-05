package com.zero.summer.example.provider;

import com.zero.summer.core.holder.UserContextHolder;
import com.zero.summer.grpc.pb.example.ExampleReply;
import com.zero.summer.grpc.pb.example.ExampleRequest;
import com.zero.summer.grpc.pb.example.ExampleServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * Example Grpc 服务提供者
 *
 * @author Zero.
 * @date 2023/3/5 12:35 PM
 */
@GRpcService
@Slf4j
public class ExampleProvider extends ExampleServiceGrpc.ExampleServiceImplBase {

    /**
     * exampleTest接口实现
     * @param request           请求参数
     * @param responseObserver  响应结果
     */
    @Override
    public void exampleTest(ExampleRequest request, StreamObserver<ExampleReply> responseObserver) {
        log.info("Request Message:{}",request.getMessage());
        log.info("UserID:{}", UserContextHolder.getUser());
        ExampleReply reply = ExampleReply.newBuilder().setResult("Hello Server").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
