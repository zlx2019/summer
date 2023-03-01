package com.zero.summer.webclient.filter;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.constant.SecurityConstant;
import com.zero.summer.core.holder.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * WebClient 全局过滤器
 *
 * @author Zero.
 * @date 2023/2/17 12:25 PM
 */
public class GlobalWebClientFilter implements ExchangeFilterFunction {

    /**
     * WebClient全局过滤器
     * 用于传递Token、UserId等信息.
     *
     * @param clientRequest 当前请求
     * @param next the next exchange function in the chain
     */
    @Override
    public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction next) {
        // 创建一个新的ClientRequest
        ClientRequest.Builder request = ClientRequest.from(clientRequest);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes attributes){
            // 获取当前线程的Request
            HttpServletRequest curRequest = attributes.getRequest();
            //从当前的请求头中取出Token,传递到下游服务
            String token = curRequest.getHeader(SecurityConstant.TOKEN_KEY);
            if (StringUtils.isNotBlank(token)){
                request.header(SecurityConstant.TOKEN_KEY,token);
            }
            // 传递链路追踪ID
            String traceId = curRequest.getHeader(Constant.TRACE_ID_HEADER);
            if (StringUtils.isNotBlank(traceId)){
                request.header(Constant.TRACE_ID_HEADER,traceId);
            }
            // 传递UserId
            request.header(Constant.USER_ID_HEADER, UserContextHolder.getUser());

            //TODO 传递其他数据...
        }
        return next.exchange(request.build());
    }
}
