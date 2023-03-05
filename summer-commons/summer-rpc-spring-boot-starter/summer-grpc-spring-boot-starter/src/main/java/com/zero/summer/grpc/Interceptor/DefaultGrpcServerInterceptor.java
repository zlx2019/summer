package com.zero.summer.grpc.Interceptor;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.exception.RpcNotAuthException;
import com.zero.summer.core.holder.UserContextHolder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * GRPC 全局服务端拦截器
 * 拦截到来自客户端的请求,将元数据内的UserId,放入用户全局会话{@link UserContextHolder}
 *
 * @author lx Zhang.
 * @date 2021/8/3 4:47 下午
 */
@Slf4j
public class DefaultGrpcServerInterceptor implements ServerInterceptor {

    /**
     * 用来传递UserId的KEY
     */
    private static final Metadata.Key<String> USER_KEY = Metadata.Key.of(Constant.RPC_USER_ID, Metadata.ASCII_STRING_MARSHALLER);
    /**
     * 用来传递traceId的Key
     */
    private static final Metadata.Key<String> TRACE_KEY = Metadata.Key.of(Constant.RPC_TRACE_KEY,Metadata.ASCII_STRING_MARSHALLER);

    /**
     * 响应码的Key
     */
    private static final Metadata.Key<String> RPC_RESULT_CODE = Metadata.Key.of(Constant.RPC_RESULT_CODE_KEY, Metadata.ASCII_STRING_MARSHALLER);

    Integer resultCode;

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        //获取客户端携带的UserID,放入全局用户信息作用域
        String userId = headers.get(USER_KEY);
        if (StringUtils.isNotBlank(userId)) {
            UserContextHolder.setUser(userId);
            // 获取traceId,放入日志组件中
            String traceId = headers.get(TRACE_KEY);
            if (StringUtils.isNotBlank(traceId)){
                MDC.put(Constant.LOG_TRACE_ID,traceId);
            }
        } else {
            //没有携带ID,未认证,禁止通信,将异常扔回客户端
            log.error("未携带UserID,你访问个球~");
            // resultCode = ResultEnum.NOT_AUTHORITY.getCode();
            // 将响应码写回
            // headers.put(RPC_RESULT_CODE, String.valueOf(resultCode));
            ServerCall.Listener<ReqT> reqTListener = next.startCall(call, headers);
            ServerCall.Listener<ReqT> listener = new ServerCall.Listener<ReqT>() {
                @Override
                public void onHalfClose() {
                    //将错误响应至客户端 方式1:
                    call.close(Status.UNAUTHENTICATED
                            .withCause(new RpcNotAuthException())
                            .withDescription("gRPC请求没有携带用户ID,未认证不能通信~"), headers);
                    // 方式2
                    //throw new GRpcRuntimeExceptionWrapper(new RpcNotAuthException(),"gRPC请求没有携带用户ID,未认证不能通信~");
                }
            };
            return listener;
        }
        ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT> serverCall = new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
            @Override
            public void sendHeaders(Metadata headers) {
                //TODO 在这里可以将一些信息写回客户端
                super.sendHeaders(headers);
            }

            /**
             * 请求结束,清除用户会话信息
             * @param status   状态
             * @param trailers 请求元数据
             */
            @Override
            public void close(Status status, Metadata trailers) {
                //移除userId等信息.
                UserContextHolder.clear();
                MDC.remove(Constant.LOG_TRACE_ID);
                super.close(status, trailers);
            }
        };
        return next.startCall(serverCall, headers);
    }
}
