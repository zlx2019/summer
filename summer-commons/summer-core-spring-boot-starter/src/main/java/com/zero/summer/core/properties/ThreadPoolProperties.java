package com.zero.summer.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 异步线程池配置
 *
 * @author Zero.
 * @date 2022/3/25 6:03 下午
 */
@Data
@ConfigurationProperties(prefix = "summer.task.pool")
public class ThreadPoolProperties {

    /**
     * 核心线程数
     */
    private int corePoolSize = 16;
    /**
     * 最大线程数
     */
    private int maxPoolSize = 96;
    /**
     * 等待队列容量
     */
    private int queueCapacity = 50;
    /**
     * 线程池中的线程名称前缀
     */
    private String threadNamePrefix = "Summer-Async-Task-";

    /**
     * ForkJoin 并行处理器核数
     */
    private Integer forkJoinParallelism = 8;

    /**
     * 多余线程存活时间 单位:秒
     */
    private int timeout = 60;
}
