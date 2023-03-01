package com.zero.summer.core.async.pool;

import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 重写Spring{@link TransmittableThreadPoolTaskExecutor}默认的异步线程池
 * 搭配{@link com.alibaba.ttl.TransmittableThreadLocal} 实现父子线程数据传递
 * 将任务执行前,通过ttl生成新的任务.
 *
 * @author Zero.
 * @date 2023/3/1 7:44 PM
 */
public class TransmittableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(@NotNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        super.execute(ttlRunnable);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        super.execute(TtlRunnable.get(task), startTimeout);
    }

    @Override
    public <T> Future<T> submit(@NotNull Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submit(ttlCallable);
    }

    @Override
    public Future<?> submit(@NotNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submit(ttlRunnable);
    }

    @Override
    public ListenableFuture<?> submitListenable(@NotNull Runnable task) {
        Runnable ttlRunnable = TtlRunnable.get(task);
        return super.submitListenable(ttlRunnable);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(@NotNull Callable<T> task) {
        Callable ttlCallable = TtlCallable.get(task);
        return super.submitListenable(ttlCallable);
    }

}
