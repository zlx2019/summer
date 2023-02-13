package com.zero.summer.cache.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.Serializable;
import java.util.Objects;

/**
 * 自定义Redis的Value序列化器
 * 基于 {@link SerializingConverter} 和 {@link DeserializingConverter}
 * 被序列化的Class对象,必须实现{@link Serializable}接口
 *
 * @author Zero.
 * @date 2023/2/13 4:45 PM
 */
public class CustomRedisValueSerializer implements RedisSerializer<Object>{

    /**
     * 序列化Null,直接返回空数组
     */
    private static final byte[] EMPTY = new byte[0];

    /**
     * Value序列化器
     * Object To byte[]
     */
    private final Converter<Object, byte[]> serializingConverter = new SerializingConverter();

    /**
     * Value反序列化器
     * byte[] To Object
     */
    private final Converter<byte[],Object> deserializingConverter = new DeserializingConverter();

    /**
     * 将Value序列化为byte[]
     * @param value     要被序列化的值
     * @return          序列化后的字节数组
     */
    @Override
    public byte[] serialize(Object value) throws SerializationException {
        if (Objects.isNull(value)){
            return EMPTY;
        }
        return this.serializingConverter.convert(value);
    }

    /**
     * 将byte[]反序列化为Object
     * @param bytes     字节数组
     * @return          Object
     * @throws SerializationException
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return this.deserializingConverter.convert(bytes);
    }
}
