package com.zero.summer.feign.config;

import com.zero.summer.feign.interceptor.GlobalFeignInterceptor;
import feign.Retryer;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * Feign 自动装配类
 * @author Zero.
 * @date 2023/2/21 8:26 PM
 */
@Import(GlobalFeignInterceptor.class)
public class FeignAutoConfigure {


    /**
     * 注入Feign的编码与解码器
     * 推荐使用 {@link SpringEncoder}，它默认实现了编码器，支持编码和解码多种格式:
     * 如JSON、XML、Form UrlEncoded、Multipart等。
     *
     * @param messageConverters 与SpringMvc转换器一致
     *
     * @return {@link Encoder}
     */
    @Bean
    public Encoder feignFormEncoder( ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    /**
     * 关闭Feign的重试机制
     * @return NEVER_RETRY 表示不重试
     */
    @Bean
    public Retryer retryer(){
        return Retryer.NEVER_RETRY;
    }
}
