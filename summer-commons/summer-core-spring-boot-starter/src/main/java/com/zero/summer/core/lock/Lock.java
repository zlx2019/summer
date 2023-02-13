package com.zero.summer.core.lock;

import java.util.concurrent.TimeUnit;

/**
 * Lock 顶级接口
 *
 * @author Zero.
 * @date 2022/4/14 18:46
 */
public interface Lock {
    /**
     * 锁的默认有效期限时长
     */
    long DEFAULT_LOCK_TIMEOUT_MILLIS = 3;

    /**
     * 加锁超时时间单位,默认为s(秒)
     */
    TimeUnit DEFAULT_TIMEOUT_UNIT = TimeUnit.SECONDS;

    /**
     * 默认失败重试次数
     */
    int DEFAULT_RETRY_COUNT = 10;

    /**
     * 默认每次重试后等待的时间 单位: ms
     */
    long DEFAULT_SLEEP_MILLIS = 200;


}
