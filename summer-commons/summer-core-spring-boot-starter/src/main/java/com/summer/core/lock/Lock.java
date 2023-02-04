package com.summer.core.lock;

/**
 * Lock 顶级接口
 *
 * @author Zero.
 * @date 2022/4/14 18:46
 */
public interface Lock {
    /**
     * 加锁默认超时时间 5 单位:ms
     */
    long DEFAULT_TIMEOUT_MILLIS = 5000;

    /**
     * 默认失败重试次数
     */
    int DEFAULT_RETRY_COUNT = 10;

    /**
     * 默认每次重试后等待的时间 单位: ms
     */
    long DEFAULT_SLEEP_MILLIS = 200;
}
