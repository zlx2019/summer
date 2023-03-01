package com.zero.summer.log.config;

import org.slf4j.TransmittableMdcAdapter;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 初始化TtlMDCAdapter实例，并替换MDC中的adapter对象
 *
 * @author Zero.
 * @date 2022/1/21 8:13 下午
 */
public class TtlMdcAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        //加载自定义的MDCAdapter实例
        TransmittableMdcAdapter.getInstance();
    }
}