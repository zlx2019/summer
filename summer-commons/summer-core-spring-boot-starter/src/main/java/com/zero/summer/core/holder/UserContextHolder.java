package com.zero.summer.core.holder;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

/**
 * Login User 全局上下文存储器
 *
 * @author Zero.
 * @date 2023/2/3 3:13 PM
 */
public class UserContextHolder {

    /**
     * 使用TransmittableThreadLocal作为存储器,支持父子线程之间的数据传递。
     * 注意: 重写 TransmittableThreadLocal的 copy() childValue()
     * 两个方法实现开启子线程时只传递父线程当时持有的数据,后续父线程操作不影响子线程
     */
    private static final ThreadLocal<String> CONTEXT = new TransmittableThreadLocal<>();

    /**
     * 设置用户信息
     * @param userId 用户ID
     */
    public static void setUser(String userId){
        CONTEXT.set(userId);
    }

    /**
     * 获取用户信息
     * @return 用户ID
     */
    public static String getUser(){
        return CONTEXT.get();
    }

    /**
     * 获取用户ID,转为Long
     * @return
     */
    public static Long getUserId(){
        return Optional.ofNullable(getUser()).map(Long::valueOf)
                .orElse(null);
    }

    /**
     * 清空当前User会话信息
     */
    public static void clear(){
        CONTEXT.remove();
    }
}
