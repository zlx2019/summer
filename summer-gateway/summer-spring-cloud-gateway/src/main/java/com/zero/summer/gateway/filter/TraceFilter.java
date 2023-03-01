package com.zero.summer.gateway.filter;

import cn.hutool.core.util.RandomUtil;
import com.zero.summer.core.constant.Constant;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 服务链路追踪ID传递过滤器
 * 在这里生成,传递到下游服务的请求头中,服务调用之前也需要传递.
 *
 * @author Zero.
 * @date 2023/3/1 10:28 PM
 */
public class TraceFilter implements GlobalFilter {

    /**
     * 生成 链路追踪ID
     * @param exchange 当前请求服务信息
     * @param chain 过滤链
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        // 生成追踪ID
        String traceId = RandomUtil.randomString(10);
        // 添加到日志信息中
        MDC.put(Constant.LOG_TRACE_ID,traceId);
        // 添加到请求头中
        request.mutate().header(Constant.TRACE_ID_HEADER,traceId);
        return chain.filter(exchange.mutate().request(request).build());
    }
}
