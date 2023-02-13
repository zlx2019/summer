package com.zero.summer.lock.config;

import com.zero.summer.lock.locks.EtcdDistributedLock;
import com.zero.summer.lock.locks.RedisDistributedLock;
import org.springframework.context.annotation.Bean;

/**
 * 分布式锁自动配置类
 *
 * @author Zero.
 * @date 2023/2/13 7:43 PM
 */
public class DistributedLockAutoConfigure {


    /**
     * 注入Redis分布式锁
     * @return {@link RedisDistributedLock}
     */
    @Bean
    public RedisDistributedLock redisDistributedLock() {
        return new RedisDistributedLock();
    }

    /**
     * 注入Etcd分布式锁
     * @return
     */
    @Bean
    public EtcdDistributedLock etcdDistributedLock(){
        return new EtcdDistributedLock();
    }

}
