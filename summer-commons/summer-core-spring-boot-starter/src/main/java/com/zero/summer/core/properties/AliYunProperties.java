package com.zero.summer.core.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 阿里云配置
 *
 * @author Zero.
 * @date 2022/4/17 13:35
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "summer.aliyun")
public class AliYunProperties {
    /**
     * 阿里云Oss配置
     */
    private Oss oss;

    /**
     * 阿里云OSS配置
     */
    @Data
    public class Oss{
        /**
         * 密钥key
         */
        private String accessKey;

        /**
         * 密钥密码
         */
        private String accessKeySecret;

        /**
         * oss 端点
         */
        private String endpoint;

        /**
         * oss bucket 名称
         */
        private String bucketName;

        /**
         * oss文件 访问前缀
         */
        private String domain;
    }
}
