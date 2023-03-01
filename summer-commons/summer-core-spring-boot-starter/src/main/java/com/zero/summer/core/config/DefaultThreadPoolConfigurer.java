package com.zero.summer.core.config;

import com.zero.summer.core.async.pool.CustomThreadPoolExecutor;
import com.zero.summer.core.async.pool.TransmittableThreadPoolTaskExecutor;
import com.zero.summer.core.properties.ThreadPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SpringBoot Task异步任务线程池默认配置
 *
 * @author Zero
 * @date 2022/3/25 6:03 下午
 */
@EnableAsync(proxyTargetClass = true)
@EnableConfigurationProperties(ThreadPoolProperties.class)
public class DefaultThreadPoolConfigurer {


    /**
     * 注入自定义异步线程池
     *
     * @return org.springframework.core.task.TaskExecutor
     **/
    @Bean
    public ThreadPoolTaskExecutor taskExecutor(ThreadPoolProperties task){
        //使用TTL实现线程任务创建,完成父子线程数据传递
        ThreadPoolTaskExecutor executor = new TransmittableThreadPoolTaskExecutor();
        executor.setCorePoolSize(task.getCorePoolSize());
        executor.setMaxPoolSize(task.getMaxPoolSize());
        executor.setQueueCapacity(task.getQueueCapacity());
        executor.setThreadNamePrefix(task.getThreadNamePrefix());
        //设置拒绝策略,当pool线程数达到MaxPoolSize时,由调用者处理该任务  || CustomThreadCallHandler 自定义拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


    /**
     * 注入自定义线程池
     *
     * @return java.util.concurrent.ExecutorService
     */
    @Bean
    public ExecutorService executorService(ThreadPoolProperties task){
        return new CustomThreadPoolExecutor(task.getCorePoolSize(),
                task.getMaxPoolSize(), task.getTimeout(), TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(task.getQueueCapacity()),
                new ThreadFactory() {
                    private final AtomicInteger threadNumber = new AtomicInteger(1);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName(task.getThreadNamePrefix() + threadNumber.getAndIncrement());
                        return thread;
                    }
                }, new ThreadPoolExecutor.CallerRunsPolicy());
    }

}
