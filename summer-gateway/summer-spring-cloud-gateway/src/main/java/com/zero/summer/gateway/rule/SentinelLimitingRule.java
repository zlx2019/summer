package com.zero.summer.gateway.rule;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.zero.summer.core.constant.ServiceConst;
import jakarta.annotation.PostConstruct;

import java.util.HashSet;
import java.util.Set;

/**
 * 编程式定义 Sentinel限流规则
 *
 * @author Zero.
 * @date 2022/7/1 4:28 PM
 */
public class SentinelLimitingRule {

    @PostConstruct
    public void initSentinel(){
        Set<GatewayFlowRule> rules = new HashSet<>();
        // 服务级别限流规则
        // 限流资源名:服务ID,注意是网关路由配置的ID
        // 规则为: summer-example-service服务 每五秒之内最多访问100次请求
        GatewayFlowRule testServiceRule = new GatewayFlowRule(ServiceConst.EXAMPLE)
                .setIntervalSec(5)//统计时间窗口,单位是秒,默认为1秒
                .setCount(100);//限流阈值
        rules.add(testServiceRule);
        GatewayRuleManager.loadRules(rules);
    }
}
