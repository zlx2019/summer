package com.zero.summer.user.config;

import com.zero.summer.core.config.SummerDefaultConfigure;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 引入核心配置类
 *
 * @author Zero.
 * @date 2023/3/1 8:03 PM
 */
@Configuration
@ControllerAdvice
public class UserServiceConfigure extends SummerDefaultConfigure {

}
