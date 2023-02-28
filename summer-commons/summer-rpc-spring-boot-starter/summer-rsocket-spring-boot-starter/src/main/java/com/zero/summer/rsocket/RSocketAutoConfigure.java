package com.zero.summer.rsocket;

import com.zero.summer.rsocket.config.RSocketConfigure;
import com.zero.summer.rsocket.factory.RSocketClientFactory;
import org.springframework.context.annotation.Import;

/**
 * RSocket 自动配置类
 *
 * @author Zero.
 * @date 2023/2/28 3:20 PM
 */
@Import({RSocketConfigure.class, RSocketClientFactory.class})
public class RSocketAutoConfigure {

}
