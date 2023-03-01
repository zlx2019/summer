package com.zero.summer.gateway.enums;

import com.zero.summer.core.constant.ResultConst;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 网关异常枚举
 *
 * @author Zero.
 * @date 2022/7/1 5:01 PM
 */
@Getter
@AllArgsConstructor
public enum GatewayError {

    NOT_FOUND(404,"【网关】你迷路了吗~",HttpStatus.NOT_FOUND),
    SERVICE_UNAVAILABLE(503,"【网关-服务】服务实例不可用,请先启动实例~",HttpStatus.SERVICE_UNAVAILABLE),
    FLOW(429,"【网关-限流】系统繁忙,请稍后再试!",HttpStatus.TOO_MANY_REQUESTS),
    FALLBACK(429,"【网关-降级】系统异常,请联系管理员",HttpStatus.TOO_MANY_REQUESTS),
    TIME_OUT(504,"【网关-超时】连接超时~",HttpStatus.GATEWAY_TIMEOUT),
    SYS_ERROR(ResultConst.FAILED_CODE,"【网关】系统异常~",HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    /** 业务响应码*/
    private final Integer code;
    /** 自定义错误消息*/
    private final String message;
    /** http响应码*/
    private final HttpStatus status;
}
