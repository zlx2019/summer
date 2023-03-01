package com.zero.summer.log.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 服务通信 链路追踪
 *
 * @author Zero.
 * @date 2023/3/1 8:48 PM
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "summer.trace")
@RefreshScope
public class TraceProperties {
    /**
     * 是否开启日志链路追踪
     */
    private Boolean enable = true;
}
