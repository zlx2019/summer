package com.zero.summer.rsocket.client;

import com.zero.summer.core.pojo.bo.UserBo;
import org.springframework.messaging.rsocket.service.RSocketExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Example服务 RSocket客户端
 *
 * @author Zero.
 * @date 2023/2/28 3:11 PM
 */
public interface ExampleSocketClient {
    /**
     * 请求/响应式
     *
     * @param entity 接收一个{@link Mono < UserBo >}.
     * @return 返回一个 {@link Mono<UserBo>}
     */
    @RSocketExchange("request/response")
    Mono<UserBo> requestAndResponse(Mono<UserBo> entity);
    /**
     * 只接受,不响应
     *
     * @return 返回一个空信号{@link Mono<Void>}
     */
    @RSocketExchange("Fire/Forget")
    Mono<Void> fireAndForget(Mono<UserBo> entity);

    /**
     * 根据指定的数量,返回一个从0递增到此数量的数据流,每秒发布一条数据
     * @param counter    数据流数值
     * @return  {@link Flux}
     */
    @RSocketExchange("request/stream")
    Flux<String> requestStream(Long counter);

    /**
     * 发送一个字符序列,会得到一个响应的全部转为大写的字符序列
     * 如: 接受一个字符序列,将字符转为大写后,将元素发送回去.
     *
     * @param in 字符序列{@link Flux<String>}
     * @return  转为大写后返回
     */
    @RSocketExchange("stream/stream")
    Flux<String> streamAndStream(Flux<String> in);
}
