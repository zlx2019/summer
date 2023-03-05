package com.zero.summer.gateway.handler;


import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.gateway.enums.GatewayError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Sentinel 统一限流异常处理
 *
 * @author Zero.
 * @date 2022/7/1 4:42 PM
 */
@Slf4j
public class SentinelBlockRequestHandler implements BlockRequestHandler {


    /**
     * 异常处理
     * @param serverWebExchange 请求与响应交互体
     * @param throwable 要处理的异常
     */
    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        // 限流枚举
        Result<Object> result = Result.Failed(GatewayError.FLOW.getMessage(),GatewayError.FLOW.getCode());
//        Map<String,Object> result = new HashMap<>();
//        result.put("message",GatewayError.FLOW.getMessage());
//        result.put("code",GatewayError.FLOW.getCode());
        return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)//响应码
                .contentType(MediaType.APPLICATION_JSON)//响应数据格式
                .bodyValue(result);//响应体
    }
}
