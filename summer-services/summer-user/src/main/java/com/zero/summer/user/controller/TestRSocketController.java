package com.zero.summer.user.controller;

import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.bo.UserBo;
import com.zero.summer.rsocket.client.ExampleSocketClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 测试RSocket服务调用
 *
 * @author Zero.
 * @date 2023/2/28 1:29 PM
 */
@RestController
@RequestMapping("/test/rsocket")
@RequiredArgsConstructor
@Slf4j
public class TestRSocketController {

    private final ExampleSocketClient exampleSocketClient;

    /**
     * 请求/响应式
     */
    @GetMapping("/r1")
    public Result<UserBo> test1(){
        UserBo userBo = exampleSocketClient.
                requestAndResponse(
                        Mono.just(new UserBo("1001", "admin")))
                .block();
        return Result.Success(userBo);
//                .subscribe(result->{
//                   log.info("请求与响应模式,Result: {}",result);
//                });
    }

    /**
     * 请求/无响应
     */
    @GetMapping("/r2")
    public void test2(){
        exampleSocketClient.fireAndForget(Mono.just(new UserBo("10012","admin2")))
                .doOnSuccess(result->{
                   log.info("请求/无响应式 Success!");
                }).subscribe();
    }

    /**
     * 请求/响应数据流
     */
    @GetMapping("/r3")
    public void test3(){
        exampleSocketClient.requestStream(20L)
                .doOnNext(message->{
                    log.info("{}",message);
                }).subscribe();
    }

    /**
     * 发送数据流/接受数据流
     *
     * 发送a,b,c....h
     * 响应A,B,C....H
     */
    @GetMapping("/r4")
    public void test4(){
        Flux<String> flux = Flux.just("a", "b", "c", "d", "e", "f", "g", "h");
        exampleSocketClient.streamAndStream(flux.delayElements(Duration.ofSeconds(1)))
                .doOnNext(s->{
                    log.info("RECEIVED: {}",s);
                }).subscribe();
    }
}
