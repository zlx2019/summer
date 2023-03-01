package com.zero.summer.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码工具类
 *
 * @author Zero.
 * @date 2022/1/23 8:44 下午
 */
public class DefaultPasswordConfig {

    /**
     * 装配BCryptPasswordEncoder用户密码的匹配
     * @return org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder()	{
        return new BCryptPasswordEncoder();
    }
}
