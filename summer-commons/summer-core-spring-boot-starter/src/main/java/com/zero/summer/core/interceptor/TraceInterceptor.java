package com.zero.summer.core.interceptor;

import com.zero.summer.core.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 链路追踪Id拦截器
 * 将Id设置到日志信息中,方便调试服务调用.
 *
 * @author Zero.
 * @date 2023/3/2 12:00 AM
 */
public class TraceInterceptor implements HandlerInterceptor {

    /**
     * 将网关中生成的traceId 或者 其他服务调用时携带的traceId 添加到MDC中,输出到日志中.
     *
     * @param request 拦截到的HTTP请求
     * @param response 拦截到的HTTP响应
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取链路追踪id
        String traceId = request.getHeader(Constant.TRACE_ID_HEADER);
        if (StringUtils.isNotBlank(traceId)){
            // 设置到日志信息中
            MDC.put(Constant.LOG_TRACE_ID,traceId);
        }
        return true;
    }

    /**
     * 释放上下文资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(Constant.LOG_TRACE_ID);
    }
}
