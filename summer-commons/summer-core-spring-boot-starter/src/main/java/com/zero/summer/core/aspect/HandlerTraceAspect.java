package com.zero.summer.example.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * 控制层方法追踪切面.
 * 请求开始及响应时分别通过日志输出请求参数以及响应参数信息等.
 * 注意: 日志级别必须开启为trace才可以.
 * logging:
 *   level:
 *     com.zero.summer: trace
 *
 * @author Zero.
 * @date 2023/2/4 8:31 PM
 */
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
@Component
public class HandlerTraceAspect {

    /**
     * 切入点范围
     * 表达式规则: @Pointcut("execution([返回值类型] [AOP要切业务类路径][当前被以及所有子包][类名][方法名]([参数类型]))")
     * 表达式解释:  包全类名为`com.zero.summer.*.controller`的所有方法,任意方法、任意方法、任意方法参数 都会被该切面所包括
     */
    @Pointcut(value = "execution(* com.zero.summer.*.controller..*.*(..))")
    public void monitor(){}


    /**
     * Spring 提供了{@link CustomizableTraceInterceptor}方法级别的跟踪拦截器.
     * 跟踪消息在方法输入时写入，如果方法调用在方法退出时成功。如果调用导致异常，则会写入异常消息。
     * 这些跟踪消息的内容是完全可自定义的，并且可以使用特殊占位符在日志消息中包含运行时信息。
     * $[methodName] - 替换为要调用的方法的名称
     * $[targetClassName] - 替换为作为调用目标的类的名称
     * $[targetClassShortName] - 替换为作为调用目标的类的短名称
     * $[returnValue] - 替换为调用返回的值
     * $[argumentTypes] - 替换为方法参数的短类名的逗号分隔列表
     * $[arguments] - 替换为方法参数表示形式的 String 逗号分隔列表
     * $[exception] - 替换为 String 调用期间提出的任何 Throwable 表示
     * $[invocationTime] - 替换为方法调用所花费的时间（以毫秒为单位）
     */
    @Bean
    public Advisor advisor(){
        // 定义拦截器
        CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
        // 设置方法进入时日志格式
        interceptor.setEnterMessage("[Controller Handler Enter] 访问目标: $[targetClassName]$[methodName]() 请求参数: $[arguments]");
        // 设置方法退出时日志格式
        interceptor.setExitMessage("[Controller Handler Exit] 访问目标: $[targetClassName]$[methodName]() 响应参数: $[returnValue] 执行时长: $[invocationTime]ms");
        // 设置方法异常时日志格式
        interceptor.setExceptionMessage("[Controller Handler Exception] 访问目标: $[targetClassName]$[methodName]() 异常信息: $[exception]");
        // 设置动态记录日志
        interceptor.setUseDynamicLogger(true);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        // 设置切入点表达式
        pointcut.setExpression("com.zero.summer.example.aspect.HandlerTraceAspect.monitor()");
        return new DefaultPointcutAdvisor(pointcut,interceptor);
    }

}
