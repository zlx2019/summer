package com.zero.summer.core.utils.column;


import com.baomidou.mybatisplus.annotation.TableField;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 对象属性工具类
 * 通过Function接口-方法推导获取实体类字段名称
 *
 * @author Zero.
 * @date 2022/3/19 6:38 下午
 */
public class ColumnUtil {

    private static final String EMPTY ="";
    private static final String DEFAULT_CAMEL_SPLIT = "_";
    private static final String GET = "get";
    private static final String IS = "is";

    /**
     * 获取实体字段原名
     *
     * @param function 属性的get方法
     * @return         字段原名称
     */
    public static <T> String getFieldName(VFunction<T,?> function){
        return getFieldName(function,EMPTY,0);
    }

    /**
     * 获取实体字段名称 返回 驼峰命名格式
     *
     * @param function 属性的get方法
     * @return         字段名称(驼峰)
     */
    public static <T> String getFieldNameToCamel(VFunction<T,?> function){
        return getFieldName(function,DEFAULT_CAMEL_SPLIT,2);
    }

    /**
     * 根据方法推导 获取该字段名称
     * @param func      属性的get方法
     * @param split     单词间分隔符 如`_`
     * @param toType    名称格式 0=原名 1=全大写 2=全小写
     * @return          字段名称
     */
    private static <T> String getFieldName(VFunction<T,?> func, String split, Integer toType){
        SerializedLambda lambda = getSerializedLambda(func);
        String implMethodName = lambda.getImplMethodName();
        int prefixLength = 0;
        if (implMethodName.startsWith(IS)){
            prefixLength = 2;
        }else if (implMethodName.startsWith(GET)){
            prefixLength = 3;
        }
        //根据get方法获取方法名
        String fieldName = Optional.of(lambda.getImplMethodName().substring(prefixLength))
                .map(name -> name.replaceFirst(name.charAt(0) + EMPTY, (name.charAt(0) + EMPTY).toLowerCase()))
                .orElseThrow(() -> new RuntimeException("get Field Name error!"));
        Field field;
        try {
            //获取Field
            field = Class.forName(lambda.getImplClass().replace("/",".")).getDeclaredField(fieldName);
        } catch (NoSuchFieldException |ClassNotFoundException e) {
            throw new RuntimeException("load Column Field error!");
        }
        //TODO
        //如果该field添加了 @CustomField注解 以注解值为准
        TableField annotation = field.getAnnotation(TableField.class);
        if (annotation!= null && StringUtils.isNotBlank(annotation.value())){
            return annotation.value();
        }
        switch (toType){
            case 1:
                return fieldName.replaceAll("[A-Z]", split + "$0").toUpperCase();
            case 2:
                return fieldName.replaceAll("[A-Z]", split + "$0").toLowerCase();
            default:
                return fieldName.replaceAll("[A-Z]", split + "$0");
        }
    }

    /**
     * 获取function的 SerializedLambda
     */
    private static <T> SerializedLambda getSerializedLambda(VFunction<T,?> function){
        Method method;
        try {
            method = function.getClass().getDeclaredMethod("writeReplace");
        }catch (NoSuchMethodException e){
            throw new RuntimeException(e);
        }
        boolean accessible = method.isAccessible();
        method.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) method.invoke(function);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(accessible);
        return serializedLambda;
    }
}
