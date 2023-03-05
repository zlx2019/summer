package com.zero.summer.user.controller;

import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.grpc.client.ExampleClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zero.
 * @date 2023/3/5 12:47 PM
 */
@RestController
@RequestMapping("/test/gRPC")
@RequiredArgsConstructor
@Slf4j
public class TestGrpcController {

    private final ExampleClient exampleClient;

    /**
     * 测试gRPC 请求
     * @return
     */
    @GetMapping("/t1")
    public Result t1(){
        String result = exampleClient.example("Hello Client");
        return Result.Success(result);
    }
}
