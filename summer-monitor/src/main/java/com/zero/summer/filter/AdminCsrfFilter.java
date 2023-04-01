package com.zero.summer.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

/**
 * Csrf 过滤器
 *
 * @author Zero.
 * @date 2023/4/1 3:06 PM
 */
public class AdminCsrfFilter extends OncePerRequestFilter {

    public static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrf != null) {

            Cookie cookie = WebUtils.getCookie(request, CSRF_COOKIE_NAME);
            String token = csrf.getToken();

            if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                cookie = new Cookie(CSRF_COOKIE_NAME, token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }

        filterChain.doFilter(request, response);
    }

}
