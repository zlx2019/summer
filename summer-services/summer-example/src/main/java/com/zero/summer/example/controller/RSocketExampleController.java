package com.zero.summer.example.controller;

import com.zero.summer.core.pojo.bo.UserBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * RSocket使用案例-服务提供者
 * 四种传输模式:
 *          Request-Response
 *          Fire-and-Forget
 *          Request-Stream
 *          Channel(Stream-Stream)
 *
 *
 * @author Zero.
 * @date 2023/2/28 4:17 PM
 */
@Controller
@Slf4j
public class RSocketExampleController {


    /**
     * 请求/响应式
     *
     * @param entity 接收一个{@link Mono<UserBo>}.
     * @return 返回一个 {@link Mono<UserBo>}
     */
    @MessageMapping("request/response")
    public Mono<UserBo> requestAndResponse(Mono<UserBo> entity){
        return entity.doOnNext(userBo -> {
            // 对参数元素进行消费(输出)
            log.info("RSocket Request-Response Success Entity: {}", userBo);
            // 然后返回一个新的元素
        }).thenReturn(new UserBo("新的ID","新的UserName"));
    }

    /**
     * 只接受,不响应
     *
     * @return 返回一个空信号{@link Mono<Void>}
     */
    @MessageMapping("Fire/Forget")
    public Mono<Void> fireAndForget(Mono<UserBo> entity){
        return entity.doOnNext(userBo -> {
            log.info("RSocket Fire-Forget Success Entity: {}", userBo);
        }).thenEmpty(Mono.empty());
    }

    /**
     * 根据指定的数量,返回一个从0递增到此数量的数据流,每秒发布一条数据
     * @param counter    数据流数值
     * @return  {@link Flux}
     */
    @MessageMapping("request/stream")
    public Flux<String> requestStream(Long counter){
        return Flux.range(0,counter.intValue())
                .map(i-> String.format("这是第%d条数据~",i))
                .delayElements(Duration.ofSeconds(1));
    }

    /**
     * 接受一个数据流,也响应一个数据流
     * 如: 接受一个字符序列,将字符转为大写后,将元素发送回去.
     *
     * @param in 字符序列{@link Flux<String>}
     * @return  转为大写后返回
     */
    @MessageMapping("stream/stream")
    public Flux<String> streamAndStream(Flux<String> in){
        return in
           .doOnNext(s -> log.info("RECEIVED: {}",s))
           .map(String::toUpperCase)
           .doOnNext(s-> log.info("SENDING: {}",s));
    }
}
