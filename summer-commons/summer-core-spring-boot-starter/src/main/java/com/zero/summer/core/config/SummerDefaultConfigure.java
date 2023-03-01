package com.zero.summer.core.config;

import org.springframework.context.annotation.Import;

/**
 * 配置类统一引用入口,服务直接继承此类并且激活即可
 *
 * @author Zero.
 * @date 2023/3/1 7:58 PM
 */
@Import({DefaultJacksonConfigurer.class,
        DefaultPasswordConfig.class,
        DefaultThreadPoolConfigurer.class,
        DefaultWebMvcConfig.class})
public class SummerDefaultConfigure {

}
