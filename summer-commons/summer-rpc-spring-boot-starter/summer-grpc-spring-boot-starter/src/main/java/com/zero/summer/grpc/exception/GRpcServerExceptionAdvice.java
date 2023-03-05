package com.zero.summer.grpc.exception;

import com.zero.summer.core.exception.BusinessException;
import com.zero.summer.core.exception.RpcNotAuthException;
import io.grpc.Status;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.recovery.GRpcExceptionHandler;
import org.lognet.springboot.grpc.recovery.GRpcExceptionScope;
import org.lognet.springboot.grpc.recovery.GRpcServiceAdvice;

import java.util.Optional;

/**
 * gRPC 服务端的异常处理
 *
 * @author Zero.
 * @date 2023/3/5 3:58 PM
 */
@GRpcServiceAdvice
@Slf4j
public class GRpcServerExceptionAdvice {

    /**
     * 自定义业务异常处理
     * 在grpc服务提供者方法里抛出 : throw new GRpcRuntimeExceptionWrapper(new BusinessException(""),"msg");
     * 则会被这里捕获
     * @param e     业务异常
     * @param scope grpc异常容器
     * @return      响应异常
     */
    @GRpcExceptionHandler
    public Status handler(BusinessException e, GRpcExceptionScope scope){
        Optional<String> message = scope.getHintAs(String.class);
        message.ifPresent(log::error);
        return Status.fromThrowable(e);
    }

    /**
     * 权限不足异常处理
     * @param e 自定义rpc权限不足异常
     */
    @GRpcExceptionHandler
    public Status handler(RpcNotAuthException e,GRpcExceptionScope scope){
        return Status.UNAUTHENTICATED;
    }

    /**
     * 未知异常处理
     * @param e     未知异常
     * @param scope grpc异常容器
     * @return      响应未知异常
     */
    @GRpcExceptionHandler
    public Status handler(RuntimeException e,GRpcExceptionScope scope){
        return Status.UNKNOWN;
    }
}
