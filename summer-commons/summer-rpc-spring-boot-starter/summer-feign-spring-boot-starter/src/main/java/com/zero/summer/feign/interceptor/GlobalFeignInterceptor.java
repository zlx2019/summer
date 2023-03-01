package com.zero.summer.feign.interceptor;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.constant.SecurityConstant;
import com.zero.summer.core.holder.UserContextHolder;
import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Feign全局拦截器
 * 服务通信时将数据传递到下游服务,如userId、token等信息
 *
 * @author Zero.
 * @date 2022/1/26 3:05 下午
 */
public class GlobalFeignInterceptor {


    /**
     * 注入Feign拦截器
     *
     * @return feign.RequestInterceptor
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return next -> {
            //获取Request,默认是无法获取,因为hystrix是默认是线程隔离,所以需要重写隔离策略后再传递
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            //传递Token
            //从当前请求头中获取Token,传递到下游服务
            Optional.ofNullable(request.getHeader(SecurityConstant.TOKEN_KEY)).map((token -> {
                next.header(SecurityConstant.TOKEN_KEY, token);
                return token;
            }));

            //传递UserID
            //从当前请求头中 or 用户信息全局存储 获取UserId,然后传递到下游服务
            String userId = UserContextHolder.getUser();
            userId = Optional.ofNullable(userId)
                            .orElse(request.getHeader(Constant.USER_ID_HEADER));
            Optional.ofNullable(userId).map(id-> {
                next.header(Constant.USER_ID_HEADER,id);
                return id;
            });

            //传递链路追踪ID
            Optional.ofNullable(request.getHeader(Constant.TRACE_ID_HEADER)).map((traceId->{
                next.header(Constant.TRACE_ID_HEADER,traceId);
                return traceId;
            }));

            //TODO 传递其他数据....
        };
    }
}
