package com.zero.summer.gateway.utils;

import com.zero.summer.core.constant.ResultConst;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.utils.JsonUtil;
import com.zero.summer.gateway.enums.GatewayError;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

/**
 * @author Zero.
 * @date 2022/7/1 12:47 PM
 */
public class ResponseUtil {

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param value 响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value)
    {
        return webFluxResponseWriter(response, HttpStatus.OK, value, ResultConst.FAILED_CODE);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param code 响应状态码
     * @param value 响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, Object value, int code)
    {
        return webFluxResponseWriter(response, HttpStatus.OK, value, code);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param status http状态码
     * @param code 响应状态码
     * @param value 响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, HttpStatus status, Object value, int code)
    {
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, status, value, code);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response ServerHttpResponse
     * @param contentType content-type
     * @param status http状态码
     * @param code 响应状态码
     * @param value 响应内容
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, Object value, int code)
    {
        response.setStatusCode(status);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        Result<?> result = Result.Failed(value.toString(),code);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtil.beanToJson(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

    /**
     * 将异常信息以json格式写会客户端
     * @param response  响应流
     * @param error     异常枚举
     */
    public static Mono<Void> responseWriter(ServerHttpResponse response, GatewayError error){
        //设置http响应码
        response.setStatusCode(error.getStatus());
        //设置响应格式为json
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        Result<Object> result = Result.Failed(error.getMessage(), error.getCode());
        // 将结果数据转为buffer
        DataBuffer resultBuffer = response.bufferFactory().wrap(JsonUtil.beanToJson(result).getBytes());
        return response.writeWith(Mono.just(resultBuffer));
    }
}
