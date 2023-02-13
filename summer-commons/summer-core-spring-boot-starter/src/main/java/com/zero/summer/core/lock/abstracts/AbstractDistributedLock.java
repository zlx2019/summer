package com.zero.summer.core.lock.abstracts;
import com.zero.summer.core.lock.DistributedLock;
import com.zero.summer.core.lock.LockResult;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁抽象类
 * @author Zero.
 * @date 2021/3/2 3:27 下午
 */
public abstract class AbstractDistributedLock implements DistributedLock {

    /**
     * 获取锁
     *
     * @param key lock key
     * @return 分布式锁结果对象 {@link LockResult}
     */
    @Override
    public LockResult lock(String key) {
        return lock(key, DEFAULT_LOCK_TIMEOUT_MILLIS,DEFAULT_TIMEOUT_UNIT, DEFAULT_RETRY_COUNT, DEFAULT_SLEEP_MILLIS);
    }

    /**
     * 获取锁
     *
     * @param key        lock key
     * @param retryCount 锁自旋次数
     * @return 分布式锁结果对象 {@link LockResult}
     */
    @Override
    public LockResult lock(String key, int retryCount) {
        return lock(key, DEFAULT_LOCK_TIMEOUT_MILLIS,DEFAULT_TIMEOUT_UNIT, retryCount, DEFAULT_SLEEP_MILLIS);
    }

    /**
     * 获取锁
     *
     * @param key         key
     * @param retryCount  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 分布式锁结果对象 {@link LockResult}
     */
    @Override
    public LockResult lock(String key, int retryCount, long sleepMillis) {
        return lock(key, DEFAULT_LOCK_TIMEOUT_MILLIS,DEFAULT_TIMEOUT_UNIT, retryCount, sleepMillis);
    }

    /**
     * 获取锁
     *
     * @param key    key
     * @param expire 锁的有效期
     * @param expireUnit 锁超时时间单位
     * @return 分布式锁结果对象 {@link LockResult}
     */
    @Override
    public LockResult lock(String key, long expire, TimeUnit expireUnit) {
        return lock(key, expire, DEFAULT_TIMEOUT_UNIT,DEFAULT_RETRY_COUNT, DEFAULT_SLEEP_MILLIS);
    }

    /**
     * 获取锁
     *
     * @param key        key
     * @param expire     锁的有效期
     * @param expireUnit 锁超时时间单位
     * @param retryCount 重试次数
     * @return 分布式锁结果对象 {@link LockResult}
     */
    @Override
    public LockResult lock(String key, long expire, TimeUnit expireUnit, int retryCount) {
        return lock(key, expire,DEFAULT_TIMEOUT_UNIT, retryCount, DEFAULT_SLEEP_MILLIS);
    }
}
