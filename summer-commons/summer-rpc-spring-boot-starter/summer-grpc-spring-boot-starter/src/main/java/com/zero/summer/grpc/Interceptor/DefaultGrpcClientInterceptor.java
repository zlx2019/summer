package com.zero.summer.grpc.Interceptor;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.holder.UserContextHolder;
import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

/**
 * Grpc 全局客户端拦截器
 * 在服务与服务之间进行GRPC调用时, 将用户ID传递到下游服务
 *
 * @author Zero.
 * @date 2021/8/3 5:49 下午
 */
@Slf4j
public class DefaultGrpcClientInterceptor implements ClientInterceptor {


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
    private static final Metadata.Key<String> RPC_RESULT_CODE = Metadata.Key.of(Constant.RPC_RESULT_CODE_KEY,Metadata.ASCII_STRING_MARSHALLER);

    /**
     * @return io.grpc.ClientCall<ReqT, RespT>
     * @apiNote 客户端拦截实现
     */
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel next) {
        return new ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(methodDescriptor, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                //传递 UserId
                String user = UserContextHolder.getUser();
                if (StringUtils.isNotBlank(user)) {
                    headers.put(USER_KEY, user);
                }
                // 传递traceId
                String traceId = MDC.get(Constant.LOG_TRACE_ID);
                if (StringUtils.isNotBlank(traceId)){
                    headers.put(TRACE_KEY,traceId);
                }
                super.start(new ForwardingClientCallListener.SimpleForwardingClientCallListener<RespT>(responseListener) {
                    @Override
                    public void onHeaders(Metadata headers) {
                        // TODO 这里可以接受服务端额外响应的参数
                        //String resultCode = headers.get(RPC_RESULT_CODE);
                        //log.info("RPC Result Code:{}",resultCode);
                        super.onHeaders(headers);
                    }
                }, headers);
            }
        };
    }
}
