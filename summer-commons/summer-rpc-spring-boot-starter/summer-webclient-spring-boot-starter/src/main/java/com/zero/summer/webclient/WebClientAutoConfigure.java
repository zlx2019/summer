package com.zero.summer.webclient;

import com.zero.summer.webclient.config.WebClientConfigure;
import com.zero.summer.webclient.config.WebClientFactoryConfigure;
import org.springframework.context.annotation.Import;

/**
 * WebClient 自动配置类
 *
 * @author Zero.
 * @date 2023/2/14 3:51 PM
 */
@Import({WebClientConfigure.class, WebClientFactoryConfigure.class})
public class WebClientAutoConfigure {

}
