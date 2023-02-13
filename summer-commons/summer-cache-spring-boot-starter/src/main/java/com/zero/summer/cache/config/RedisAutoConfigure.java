package com.zero.summer.cache.config;

import com.zero.summer.cache.convert.CustomRedisValueSerializer;
import com.zero.summer.cache.template.CacheTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 自动装配类
 * @author Zero.
 * @date 2023/2/13 5:47 PM
 */
@EnableConfigurationProperties(RedisProperties.class)
@EnableCaching
public class RedisAutoConfigure {

    /**
     * 注入 RedisTemplate模板工具
     * @param factory   基于Lettuce异步连接池
     * @date 2023/2/13 5:47 PM
     * @return org.springframework.data.redis.core.RedisTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory factory){
        // 设置连接工厂
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 设置Key与Value序列化器
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        CustomRedisValueSerializer valueSerializer = new CustomRedisValueSerializer();
        redisTemplate.setKeySerializer(keySerializer);
        redisTemplate.setHashKeySerializer(keySerializer);
        redisTemplate.setValueSerializer(valueSerializer);
        redisTemplate.setHashValueSerializer(valueSerializer);
        //初始化
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 注入CacheTemplate自定义模板工具类
     * @return com.zero.summer.cache.template.CacheTemplate
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheTemplate cacheTemplate(RedisTemplate<String, Object> redisTemplate) {
        return new CacheTemplate(redisTemplate);
    }
}
