package com.zero.summer.core.interceptor;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.holder.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 用户信息拦截器,用于拦截请求头中的一些信息,设置到当前全局会话中
 *
 * @author Zero.
 * @date 2023/2/22 12:49 AM
 */
@Slf4j
public class UserInfoInterceptor implements HandlerInterceptor {

    /**
     * 拦截请求头中的UserID,放入{@link com.zero.summer.core.holder.UserContextHolder}中
     * @param request 拦截到的HTTP请求
     * @param response 拦截到的HTTP响应
     * @return  true
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户ID
        String userId = request.getHeader(Constant.USER_ID_HEADER);
        if (StringUtils.isNotBlank(userId)){
            //将用户ID,注入到全局上下文中
            UserContextHolder.setUser(userId);
        }
        return true;
    }

    /**
     * 释放上下文资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //释放当前会话用户信息
        UserContextHolder.clear();
    }
}
