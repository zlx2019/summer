package com.zero.summer.gateway.handler;


import com.zero.summer.gateway.enums.GatewayError;
import com.zero.summer.gateway.utils.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * SpringCloudGateway 统一异常处理
 * 一定要指定优先级,不然可能会失效
 *
 * @author Zero.
 * @date 2022/1/30 9:36 下午
 */
@Order(-1)
public class GatewayExceptionHandler implements ErrorWebExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GatewayExceptionHandler.class);

    /**
     * 异常处理逻辑
     * @param exchange the current exchange
     * @param e 要处理的异常
     */
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        ServerHttpResponse response = exchange.getResponse();
        if (exchange.getResponse().isCommitted()){
            return Mono.error(e);
        }
        GatewayError error = GatewayError.SYS_ERROR;
        if (e instanceof NotFoundException){
            // 找不到可用服务实例
            error = GatewayError.SERVICE_UNAVAILABLE;
        } else if (e instanceof ResponseStatusException){
            ResponseStatusException exception = (ResponseStatusException)e;
            // 404找不到资源错误
            if (exception.getStatusCode().value() == GatewayError.NOT_FOUND.getCode()){
                error = GatewayError.NOT_FOUND;
            }
            // 连接下游服务超时
            if (exception.getRootCause() instanceof TimeoutException){
                error = GatewayError.TIME_OUT;
            }
        }
        log.error("[网关异常处理]请求路径:{},异常信息:{}", exchange.getRequest().getPath(), e.getMessage());
        return ResponseUtil.responseWriter(response,error);
    }
}
