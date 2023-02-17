package com.zero.summer.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.zero.summer.core.constant.Constant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * JSON 工具类
 * 配置参考 https://www.cnblogs.com/scar1et/articles/14134024.html
 *
 * @author Zero.
 * @date 2021/10/20 10:23 上午
 */
@Slf4j
public class JsonUtil {

    /**
     * Jackson 实例
     */
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        // 序列化时,对象属性为NULL的不序列化
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时 忽略json中存在 但对象中不存在的字段
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 支持序列化时 实体为驼峰 Json为下划线小写分割
        // 反序列化时,Json为下划线小写分割、实体为驼峰命名
        MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);

        // 允许出现特殊字符和转义符
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许出现单引号
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(Constant.DATETIME_FORMAT)));
        MAPPER.registerModule(javaTimeModule);
    }


    /**
     * Object To Json
     *
     * @param bean  Object
     * @param <T>   泛型
     * @return      Json
     */
    @SneakyThrows
    public static <T> String beanToJson(T bean) {
        return MAPPER.writeValueAsString(bean);
    }


    /**
     * Json To Object
     * @param value Json
     * @param clazz 泛型
     * @return      Object
     */
    @SneakyThrows
    public static <T> T readValueToBean(String value, Class<T> clazz) {
        return MAPPER.readValue(value, clazz);
    }


    /**
     * Json To Collection
     *
     * @param value    Json
     * @param listType 集合类型
     * @param clazz    集合泛型
     * @return
     */
    @SneakyThrows
    public static <T, L extends Collection> L readValueToList(String value, Class<L> listType, Class<T> clazz) {
        CollectionType collectionType = MAPPER.getTypeFactory().constructCollectionType(listType, clazz);
        return MAPPER.readValue(value, collectionType);
    }

    /**
     * Json To List
     * @param value Json
     * @param clazz 集合泛型
     * @return      List
     */
    @SneakyThrows
    public static <T> List<T> readValueToList(String value, Class<T> clazz){
        CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
        return MAPPER.readValue(value,listType);
    }

    /**
     * Json To Map
     *
     * @param value     Json
     * @param mapType   Map 类型
     * @param keyType   Key 类型
     * @param valueType Value 类型
     * @return Map容器
     */
    @SneakyThrows
    public static  <M extends Map, K, V> M readValueToMap(String value, Class<M> mapType, Class<K> keyType, Class<V> valueType) {
        MapType mapKind = MAPPER.getTypeFactory().constructMapType(mapType, keyType, valueType);
        return MAPPER.readValue(value, mapKind);
    }
}
