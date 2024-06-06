package com.zero.summer.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁顶级接口
 *
 * @author Zero.
 * @date 2022/4/14 20:53
 */
public interface DistributedLock extends Lock {

    /**
     * 获取锁
     *
     * @param key lock key
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key);

    /**
     * 获取锁
     *
     * @param key        lock key
     * @param retryCount 锁自旋次数
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key, int retryCount);

    /**
     * 获取锁
     *
     * @param key         key
     * @param retryCount  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key, int retryCount, long sleepMillis);

    /**
     * 获取锁
     *
     * @param key    key
     * @param expire 获取锁超时时间
     * @param expireUnit 锁超时时间单位
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key, long expire, TimeUnit expireUnit);

    /**
     * 获取锁
     *
     * @param key        key
     * @param expire     获取锁超时时间
     * @param expireUnit 锁超时时间单位
     * @param retryCount 重试次数
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key, long expire,TimeUnit expireUnit, int retryCount);

    /**
     * 获取锁(自旋+休眠)
     *
     * @param key         key
     * @param expire      锁的有效期
     * @param expireUnit 锁超时时间单位
     * @param retryCount  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 分布式锁结果对象 {@link LockResult}
     */
    LockResult lock(String key, long expire,TimeUnit expireUnit, int retryCount, long sleepMillis);


    //TODO 获取锁(阻塞式+公平锁)



    /**
     * 释放锁
     * @param lockResult  加锁结果对象
     * @return  释放结果
     */
    boolean releaseLock(LockResult lockResult);

}
