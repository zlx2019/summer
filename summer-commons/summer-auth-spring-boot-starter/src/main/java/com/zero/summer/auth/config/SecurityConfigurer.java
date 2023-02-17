package com.zero.summer.auth.config;

import com.zero.summer.auth.filter.TokenAuthorizationFilter;
import com.zero.summer.auth.handler.AccessDeniedExceptionHandler;
import com.zero.summer.auth.handler.AuthenticationExceptionHandler;
import com.zero.summer.auth.service.IUserDetailsService;
import com.zero.summer.core.constant.SecurityConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity配置
 *
 * @author Zero.
 * @date 2023/2/16 11:14 PM
 */
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfigurer  {

    /**
     * 自定义用户获取处理接口
     */
    private final IUserDetailsService userDetailsService;
    /**
     * 自定义Token鉴权过滤器
     */
    private final TokenAuthorizationFilter tokenAuthorizationFilter;

    /**
     * 密码加密器
     */
    private final PasswordEncoder passwordEncoder;


    /**
     * 注入身份验证管理器,使用Spring默认的实现
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * 配置认证处理接口,及密码加密器
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 设置获取根据用户名获取用户信息的接口
        provider.setUserDetailsService(userDetailsService);
        // 设置密码器
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    /**
     * 注入自定义Token鉴权异常处理
     * @return {@link AuthenticationExceptionHandler}
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationExceptionHandler();
    }

    /**
     * 注入自定义权限不足异常处理
     * @return {@link AccessDeniedExceptionHandler}
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new AccessDeniedExceptionHandler();
    }

    /**
     * 配置自定义的过滤链等信息.
     * 如认证过滤器、鉴权过滤器、黑白名单、异常处理。
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()// 关闭csrf请求
                // 请求权限配置
                .authorizeHttpRequests()
                // 配置白名单
                //requestMatchers()设置白名单uri列表
                // permitAll() 表示可以全部直接通过,不经过拦截器
                // 所有OPTIONS请求也全部放行
                    .requestMatchers(SecurityConstant.EXCLUDE_URL).permitAll()
                    .requestMatchers(HttpMethod.OPTIONS).permitAll()
                // anyRequest() 表示除了白名单之外的其他的所有请求
                // authenticated() 必须要经过拦截器认证
                    .anyRequest().authenticated()
                .and()
                // 会话策略: Session为无状态模式
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 设置自定义的认证过滤器
                    .authenticationProvider(authenticationProvider())
                // 设置自定义的 Token校验过滤器
                    .addFilterBefore(tokenAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // 设置指定认证鉴权异常处理类
                .authenticationEntryPoint(authenticationEntryPoint())
                // 设置权限不足异常处理类
                .accessDeniedHandler(accessDeniedHandler());
        return http.build();
    }
}
