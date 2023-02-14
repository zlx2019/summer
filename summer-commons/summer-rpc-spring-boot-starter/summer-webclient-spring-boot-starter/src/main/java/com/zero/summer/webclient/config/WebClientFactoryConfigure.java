package com.zero.summer.webclient.config;

import com.zero.summer.webclient.factory.UserClientFactory;
import org.springframework.context.annotation.Import;

/**
 * 客户端接口注入工厂 统一在此类依赖注入,方便管理
 *
 * @author Zero.
 * @date 2023/2/14 6:09 PM
 */
@Import({UserClientFactory.class})
public class WebClientFactoryConfigure {
}
