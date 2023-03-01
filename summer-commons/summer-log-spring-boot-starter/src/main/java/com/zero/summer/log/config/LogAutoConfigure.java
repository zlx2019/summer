package com.zero.summer.log.config;

import com.zero.summer.log.properties.TraceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Log组件自动配置类
 *
 * @author Zero.
 * @date 2023/3/1 8:52 PM
 */
@EnableConfigurationProperties(TraceProperties.class)
public class LogAutoConfigure {

}
