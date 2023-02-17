package com.zero.summer.auth.filter;

import com.zero.summer.auth.utils.TokenUtil;
import com.zero.summer.core.constant.Constant;
import com.zero.summer.core.constant.SecurityConstant;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.enums.ResultEnum;
import com.zero.summer.core.utils.JsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 鉴权过滤器,用来校验JWT Token是否有效.
 * 继承于{@link OncePerRequestFilter} 它能实现每个请求只会过滤一次,确保不会多次过滤.
 *
 * @author Zero.
 * @date 2023/2/16 4:32 PM
 */
@RequiredArgsConstructor
@Configuration
@Slf4j
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    /**
     * 自定义用户信息查询接口
     */
    private final UserDetailsService userDetailsService;
    /**
     * 白名单匹配器
     */
    private static  final AntPathMatcher MATCH = new AntPathMatcher();
    /**
     * 白名单
     */
    private static final List<String> ALLOW_URL = List.of(SecurityConstant.EXCLUDE_URL);

    /**
     * 与 的 doFilter协定相同，但保证在单个请求线程中每个请求仅调用一次
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤链
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        // TODO 因为需要抛出详细的异常信息,如果直接强制响应客户端,会导致白名单失效.
        // TODO 所以这里自己也校验是否是白名单,白名单直接跳过,然后进入过滤链.否则将无法抛出详细的异常信息.
        boolean isAllow = ALLOW_URL.stream().anyMatch(url-> MATCH.match(url,request.getRequestURI()));
        if (isAllow){
            //白名单直接放行
            filterChain.doFilter(request, response);
        }else {
            final String authorization = request.getHeader(SecurityConstant.TOKEN_KEY);
            if (StringUtils.isBlank(authorization)) {
                responseHandler("未提供Token~",response);
                return;
            }
            // 校验Token及前缀
            if (!authorization.startsWith(SecurityConstant.TOKEN_PREFIX)) {
                responseHandler("Token前缀不正确~", response);
                return;
            }
            // 去除前缀
            final String token = authorization.replaceAll(SecurityConstant.TOKEN_PREFIX, "");
            Claims claims = null;
            try {
                // 解析Token为Claims
                claims = TokenUtil.parseToken(token);
            } catch (Exception e) {
                if (e instanceof ExpiredJwtException expiredJwtException) {
                    //通过 expiredJwtException 可以获取过期时间
                    responseHandler("Token已过期~", response);
                } else if (e instanceof SignatureException signatureException) {
                    responseHandler("认证失败,Token不可信任~", response);
                }
                return;
            }
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
            // 校验Token解析出的Username用户是否存在于数据库
            if (Objects.isNull(userDetails)){
                responseHandler("Token的用户不存在~",response);
                return;
            }
                // 校验通过,生成认证信息
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, new ArrayList<>());
                // 从当前http请求中 读取有关当前请求的信息
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 将认证信息设置到Security上下文
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
        }

        /**
         * 认证失败,根据不同的原因,自定义响应信息处理.
         * @param message   响应消息
         * @param response  响应流
         */
        private void responseHandler (String message, HttpServletResponse response) throws IOException {
            log.error("认证异常:{}", message);
            // 设置响应头
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Cache-Control", "no-cache");
            response.setCharacterEncoding(Constant.CHAR_SET);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            // 认证失败,响应码统一为401
            int code = ResultEnum.AUTH_LOGIN_ERR.getCode();
            response.setStatus(code);
            // 序列化响应消息
            String responseMessage = JsonUtil.beanToJson(Result.Failed(message, code));
            response.getWriter().write(responseMessage);
            response.getWriter().flush();
        }
    }
