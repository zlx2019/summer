package com.zero.summer.webclient;

import com.zero.summer.webclient.config.WebClientConfigure;
import com.zero.summer.webclient.config.WebClientFactoryConfigure;
import org.springframework.context.annotation.Import;

/**
 * WebClient 自动配置类
 * TODO 测试 Bean Import 扫描 的优先级
 *
 * @author Zero.
 * @date 2023/2/14 3:51 PM
 */
@Import({WebClientConfigure.class, WebClientFactoryConfigure.class})
public class WebClientAutoConfigure {

}
