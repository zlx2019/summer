package com.zero.summer.security;

import com.zero.summer.filter.AdminCsrfFilter;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

/**
 * 监控服务的安全配置,直接基于内存方式定义管理员角色即可
 *
 * @author Zero.
 * @date 2023/4/1 3:02 PM
 */
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfigure {

    private final AdminServerProperties adminServer;
    private final SecurityProperties security;

    /**
     * 服务安全配置
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        // 默认转发到 /
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

        // 将白名单暴露出去
        http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests
                .requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/assets/**")))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/actuator/info")))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(adminServer.path("/actuator/health")))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/login")))
                .permitAll()
                .dispatcherTypeMatchers(DispatcherType.ASYNC)
                .permitAll() // https://github.com/spring-projects/spring-security/issues/11027
                // 其余请求全部需要认证
                .anyRequest().authenticated())
                // 使用默认登录表单
                .formLogin(
                        (formLogin) -> formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler))
                .logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
                .httpBasic(Customizer.withDefaults());

        //使用 Cookie 启用 CSRF 保护
        http.addFilterAfter(new AdminCsrfFilter(), BasicAuthenticationFilter.class)
                .csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
        new AntPathRequestMatcher(this.adminServer.path("/instances"), POST.toString()),
        new AntPathRequestMatcher(this.adminServer.path("/instances/*"), DELETE.toString()),
        new AntPathRequestMatcher(this.adminServer.path("/actuator/**"))));
        http.rememberMe((rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));
        return http.build();
    }

    /**
     * 生成内存用户-账户和密码
     * @param passwordEncoder 密码器
     * @return 临时内存用户角色
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("summer-admin").password(passwordEncoder.encode("123456")).roles("USER").build();
        return new InMemoryUserDetailsManager(user);
    }

    /**
     * 注入密码器
     * @return {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
