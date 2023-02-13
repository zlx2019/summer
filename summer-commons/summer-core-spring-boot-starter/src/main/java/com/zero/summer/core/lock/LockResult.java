package com.zero.summer.core.lock;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用于描述分布式锁结果
 * 同时包装一些解锁所需的参数
 *
 * @author Zero.
 * @date 2023/2/13 9:53 PM
 */
@Data
@Accessors(chain = true)
public class LockResult implements Serializable {

    private static final long serialVersionUID = 6645428376906493261L;
    /**
     * 加锁成功与失败常量
     */
    private static final LockResult SUCCESS = new LockResult().setLock(Boolean.TRUE);
    private static final LockResult FAIL = new LockResult().setLock(Boolean.FALSE);


    /**
     * 锁的标识
     */
    private String key;
    /**
     * 加锁是否成功
     */
    private boolean lock;
    /**
     * 租约的ID-etcd分布式锁需要用到
     */
    private long leaseId;

    /**
     * 构建一个成功或者失败响应
     * @param lock
     * @return
     */
    public static LockResult of(boolean lock){
        return lock ? SUCCESS : FAIL;
    }

}
