package com.zero.summer.lock.locks;

import com.zero.summer.core.lock.LockResult;
import com.zero.summer.core.lock.abstracts.AbstractDistributedLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的分布式锁实现
 * @author Zero.
 * @date 2022/1/19 9:27 下午
 */
@Slf4j
public class RedisDistributedLock extends AbstractDistributedLock {

    @Autowired
    private RedisTemplate redisTemplate;
    /** 用于存放钥匙 线程隔离,防止删除其他线程的锁*/
    private ThreadLocal<String> lockTag = new ThreadLocal<>();

    /** 使用Lua脚本释放锁*/
    private static final String UNLOCK_LAU;
    static {
        UNLOCK_LAU = "if redis.call(\"get\",KEYS[1]) == ARGV[1] " +
                "then " +
                "    return redis.call(\"del\",KEYS[1]) " +
                "else " +
                "    return 0 " +
                "end ";
    }

    /**
     * 获取锁/加锁
     *
     * @param key         key
     * @param expire      锁的有效期
     * @param expireUnit 锁超时时间单位
     * @param spinNum     重试次数
     * @param sleepMillis 获取锁失败的重试间隔
     * @return 成功/失败
     */
    @Override
    public LockResult lock(String key, long expire, TimeUnit expireUnit, int spinNum, long sleepMillis) {
        boolean lockResult = upLock(key, expire,expireUnit);
        //加锁失败且有剩余次数 进行自旋
        while ((!lockResult) && spinNum-- > 0){
            try {
                log.debug("【{}】加锁失败,剩余重试次数:{}",key,spinNum);
                TimeUnit.MILLISECONDS.sleep(sleepMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            lockResult = upLock(key,expire,expireUnit);
        }
        log.info("加锁完成，tag:{}",lockTag.get());
        return LockResult.of(lockResult).setKey(key);
    }

    /**
     * 加锁
     * @param key 键
     * @param expire 加锁时长
     * @param expireUnit 加锁时长时间单位
     * @return 是否加锁成功
     */
    private boolean upLock(final String key,final long expire,TimeUnit expireUnit){
        try {
            return (boolean) redisTemplate.execute((RedisCallback<Boolean>) conn -> {
                //每个锁生成一个唯一标识,以免释放错误
                String tag = UUID.randomUUID().toString();
                lockTag.set(tag);
                RedisSerializer stringSerializer = redisTemplate.getStringSerializer();
                byte[] keyBytes = stringSerializer.serialize(key);
                byte[] tagBytes = stringSerializer.serialize(tag);
                return conn.set(keyBytes,tagBytes,Expiration.from(expire,expireUnit),RedisStringCommands.SetOption.ifAbsent());
            });
        }catch (Exception e){
            log.error("Redis DistributedLock Error!");
            return false;
        }
    }


    /**
     * 释放锁/解锁
     *
     * @param lockResult 加锁结果对象,从中获取要释放的锁
     * @return 释放结果
     */
    @Override
    public boolean releaseLock(LockResult lockResult) {
        try {
            return (boolean) redisTemplate.execute((RedisCallback<Boolean>) connection->{
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] luaBytes = redisTemplate.getStringSerializer().serialize(UNLOCK_LAU);
                // 使用lua脚本删除redis中匹配tag的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
                // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
                return connection.eval(luaBytes, ReturnType.BOOLEAN, 1, serializer.serialize(lockResult.getKey()), serializer.serialize(lockTag.get()));
            });
        } catch (Exception e) {
            log.error("key:{},释放锁异常:{}",lockResult.getKey(),e);
        } finally {
            lockTag.remove();//移除tag
        }
        return false;
    }
}
