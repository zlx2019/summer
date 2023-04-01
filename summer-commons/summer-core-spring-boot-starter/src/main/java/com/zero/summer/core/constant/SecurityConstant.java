package com.zero.summer.core.constant;

/**
 * Spring Security常量
 *
 * @author Zero.
 * @date 2023/2/16 11:46 PM
 */
public interface SecurityConstant {

    /**
     * SECRET: 加密盐
     * EXPIRED_TIME: jwt 令牌有效期 / 10 days
     * TOKEN_PREFIX: 令牌前缀
     * TOKEN_KEY: 令牌在请求头存储的Key
     * EXCLUDE_URL: 白名单api数组
     */
    String SECRET = "7A24432646294A404E635266556A576E5A7234753778214125442A472D4B6150";
    long EXPIRED_TIME = 60 * 60 * 24 * 10 * 1000;
    String TOKEN_PREFIX = "Bearer ";
    String TOKEN_KEY = "Authorization";
    // 服务监控白名单
    String[] EXCLUDE_URL = new String[]{"/user/**","/summer/not_found/**","/assets/**","/actuator/**"};

}
