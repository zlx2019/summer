package com.zero.summer.core.config;

import com.zero.summer.core.interceptor.UserInfoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 默认SpringMVC拦截器
 * @apiNote  不再通过自WebMvcConfigurationSupport注册拦截器,会导致一些配置失效
 *
 * @author lx Zhang.
 * @date 2021/3/2 3:34 下午
 */
@Configuration
public class DefaultWebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加拦截器
     *
     * @Author lx Zhang.
     * @Date 2021/3/2 3:35 下午
     * @Param [registry]
     **/
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加用户拦截器
        registry.addInterceptor(new UserInfoInterceptor()).addPathPatterns("/**");
    }


    /**
     * @apiNote 配置swagger资源对外开放, 防止造成无法访问
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
