package com.summer.core.lock;

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
     * @return 成功/失败
     */
    boolean lock(String key);

    /**
     * 获取锁
     *
     * @param key        lock key
     * @param retryCount 锁自旋次数
     * @return 成功/失败
     */
    boolean lock(String key, int retryCount);

    /**
     * 获取锁
     *
     * @param key         key
     * @param retryCount  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 成功/失败
     */
    boolean lock(String key, int retryCount, long sleepMillis);

    /**
     * 获取锁
     *
     * @param key    key
     * @param expire 获取锁超时时间
     * @return 成功/失败
     */
    boolean lock(String key, long expire);

    /**
     * 获取锁
     *
     * @param key        key
     * @param expire     获取锁超时时间
     * @param retryCount 重试次数
     * @return 成功/失败
     */
    boolean lock(String key, long expire, int retryCount);

    /**
     * 获取锁
     *
     * @param key         key
     * @param expire      获取锁超时时间
     * @param retryTimes  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 成功/失败
     */
    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 释放锁
     *
     * @param key key值
     * @return 释放结果
     */
    boolean releaseLock(String key);
}
