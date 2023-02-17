package com.zero.summer.auth.handler;

import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.enums.ResultEnum;
import com.zero.summer.core.utils.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * 自定义Spring Security认证异常处理
 * 未携带Token、Token校验错误、已失效等响应该异常.
 *
 * @author Zero.
 * @date 2023/2/17 9:39 AM
 */
@Slf4j
public class AuthenticationExceptionHandler implements AuthenticationEntryPoint {

    /**
     * 统一身份验证异常处理
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error("认证失败:",authException);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding(Constant.CHAR_SET);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 认证失败 401.
        response.setStatus(ResultEnum.AUTH_LOGIN_ERR.getCode());
        Result result = Result.Failed(ResultEnum.AUTH_LOGIN_ERR);
        response.getWriter().println(JsonUtil.beanToJson(result));
        response.getWriter().flush();
    }
}
