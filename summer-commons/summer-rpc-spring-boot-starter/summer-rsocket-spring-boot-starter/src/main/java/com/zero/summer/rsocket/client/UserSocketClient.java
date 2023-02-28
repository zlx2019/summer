package com.zero.summer.rsocket.client;

import com.zero.summer.core.entity.User;
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Mono;

/**
 * User服务 RSocket客户端
 *
 * @author Zero.
 * @date 2023/2/28 3:10 PM
 */
public interface UserSocketClient {

    @RSocketExchange("hello/test")
    Mono<String> hello(User user);
}
