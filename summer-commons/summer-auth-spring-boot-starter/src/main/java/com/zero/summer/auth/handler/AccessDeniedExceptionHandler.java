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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 自定义Spring Security认证异常处理
 * 用户权限不足时抛出该异常
 *
 * @author Zero.
 * @date 2023/2/17 9:47 AM
 */
@Slf4j
public class AccessDeniedExceptionHandler implements AccessDeniedHandler {

    /**
     * 统一响应 权限不足,访问失败异常。
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("权限不足,无法访问",accessDeniedException);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control","no-cache");
        response.setCharacterEncoding(Constant.CHAR_SET);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        // 权限不足 403.
        response.setStatus(ResultEnum.NOT_AUTHORIZED_ERR.getCode());
        Result result = Result.Failed(ResultEnum.NOT_AUTHORIZED_ERR);
        response.getWriter().println(JsonUtil.beanToJson(result));
        response.getWriter().flush();
    }
}
