package com.zero.summer.core.exception;


/** 自定义GRPC调用 权限不足异常
 * @author lx Zhang.
 * @date 2021/8/4 2:05 下午
 */
public class RpcNotAuthException extends RuntimeException{
    public RpcNotAuthException(){}
    public RpcNotAuthException(String msg) {
        super(msg);
    }
}
