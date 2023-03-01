package com.zero.summer.gateway.rule;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.zero.summer.core.constant.ServiceConst;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

/**
 * 编程式定义 Sentinel降级规则
 * API文档: <a href="https://sentinelguard.io/zh-cn/docs/circuit-breaking.html"/>
 *
 * @author Zero.
 * @date 2022/7/1 5:54 PM
 */
public class SentinelDegradeRule {

    @PostConstruct
    public void initDegradeRule(){
        List<DegradeRule> rules = new ArrayList<>();

        // 熔断资源名:服务ID,注意是网关路由配置的ID
        // 针对ID为 summer-example-service服务进行降级。降级模式为异常计数
        // 如果路由到zeus-test服务的请求,10秒内 请求了5次,其中有3次都异常,则进行熔断该服务

        // TODO 异常如果被处理了则不算异常计数
        DegradeRule rule = new DegradeRule(ServiceConst.EXAMPLE) //资源名
                .setGrade(2) //断路策略（0：平均RT，1：异常比率，2：异常计数）。
                .setCount(10)//慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）；异常比例/异常数模式下为对应的阈值
                .setTimeWindow(10)//熔断时长,单位为秒
                .setMinRequestAmount(3)//熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断（1.7.0 引入）
                .setStatIntervalMs(10 * 1000);//统计时长（单位为 ms）
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);

    }
}
