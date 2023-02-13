package com.zero.summer.lock.locks;

import com.zero.summer.core.lock.LockResult;
import com.zero.summer.core.lock.abstracts.AbstractDistributedLock;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.Lease;
import io.etcd.jetcd.Lock;
import io.etcd.jetcd.lock.LockResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 基于Etcd的分布式锁实现
 *
 * @author Zero.
 * @date 2023/2/13 8:03 PM
 */
@Slf4j
public class EtcdDistributedLock extends AbstractDistributedLock {

    @Autowired
    private Client client;
    private Lock lockClient;
    private Lease leaseClient;

    @PostConstruct
    public void init(){
        lockClient = client.getLockClient();
        leaseClient = client.getLeaseClient();
    }

    /**
     * 获取锁(自选式)
     *
     * @param key         key
     * @param expire      获取锁超时时间
     * @param retryCount  重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 成功/失败
     */
    @Override
    public LockResult lock(String key, long expire, TimeUnit expireUnit, int retryCount, long sleepMillis) {
        // 尝试加锁
        LockResult lockResult =  uplock(key,expire,expireUnit,sleepMillis);
        while ((!lockResult.isLock()) && retryCount-- >0){
            // 如果加锁失败,并且还有可重试次数
            log.error("Etcd 加锁失败,剩余重试次数:{}",retryCount);
            lockResult = uplock(key, expire, expireUnit, sleepMillis);
        }
        log.info("Etcd 加锁完成 结果:{}",lockResult);
        return lockResult;
    }


    /**
     * 尝试加锁
     * @param key 锁标识
     * @param expire 加锁时长
     * @param expireUnit 加锁时长时间单位
     * @param sleepMillis 加锁自旋间隔时长
     * @return LockResult 加锁结果对象
     */
    private LockResult uplock(String key, long expire, TimeUnit expireUnit,long sleepMillis) {
        // Etcd租约时长单位默认为秒,转换一下
        expire = expireUnit.toSeconds(expire);
        if (expire <= 0){
            // 最少为1秒
            expire = 1;
        }
        // 加锁结果对象
        LockResult lockResult = new LockResult().setLock(Boolean.FALSE);
        long leaseId;
        try {
            // 创建租约
            leaseId = leaseClient.grant(expire).get().getID();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Etcd 创建锁的租约异常:",e);
            return lockResult;
        }
        // 加锁
        try {
            ByteSequence lockKey = ByteSequence.from(key, StandardCharsets.UTF_8);
            LockResponse response = lockClient.lock(lockKey, leaseId)
                    .get(sleepMillis, TimeUnit.MILLISECONDS);
            log.info("Etcd 加锁成功 Key:{} Lock-Key:{}",key,response.getKey());
            lockResult.setLock(Boolean.TRUE).setLeaseId(leaseId).setKey(key);
        } catch (TimeoutException e) {
            // 超时后,进行重试
            log.error("Etcd 加锁超时 Key:{}",key);
            leaseClient.revoke(leaseId);
            return lockResult;
        } catch (InterruptedException | ExecutionException e){
            log.error("Etcd 加锁异常 Error",e);
            Thread.currentThread().interrupt();
        }
        return lockResult;
    }


    /**
     * 释放锁
     *
     * @param lockResult 需要释放的锁对象
     * @return 释放结果
     */
    public boolean releaseLock(LockResult lockResult) {
        if (lockResult.isLock()){
            try {
                lockClient.unlock(ByteSequence.from(lockResult.getKey(),StandardCharsets.UTF_8));
                leaseClient.revoke(lockResult.getLeaseId());
                log.info("释放锁完成 Key:{}",lockResult.getKey().toString());
                return Boolean.TRUE;
            }catch (Exception e){
                log.error("Etce 释放锁异常:",e);
                return false;
            }
        }
        return true;
    }
}
