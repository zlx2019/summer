package com.zero.summer.core.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 对象拷贝工具
 *
 * @author Zero.
 * @date 2023/2/3 8:21 PM
 */
@Slf4j
public class CloneUtil {

    /**
     * 属性浅拷贝.
     * 将source的所有属性值,拷贝到target的属性上,根据属性名拷贝.
     *
     * @param source        源对象
     * @param targetClazz   目标对象Class
     * @return              拷贝后得到的新对象
     * @param <S>           源对象泛型
     * @param <R>           目标对象泛型Class
     */
    public static <S,R> R copyValue(S source,Class<R> targetClazz){
        try {
            R newInstance = targetClazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source,newInstance);
            return newInstance;
        }catch (Exception e){
            log.error("Bean Clone Error:{}",e.getMessage());
            return null;
        }
    }

    /**
     * 属性浅拷贝,忽略source的Null值
     *
     * @param source    源对象
     * @param target    目标对象
     */
    public static <S,R> void copyValue(S source,R target){
        BeanUtil.copyProperties(source,target, CopyOptions.create().ignoreNullValue().ignoreError());
    }

    /**
     * 属性浅拷贝.
     * 将source的所有属性值,拷贝到target的属性上,根据属性名拷贝.
     *
     * @param source          源对象
     * @param targetClazz     目标对象Class
     * @param ignoreProsNames 需要忽略的属性
     * @return                拷贝后得到的新对象
     * @param <S>             源对象泛型
     * @param <R>             目标对象泛型Class
     */
    public static <S,R> R copyValue(S source,Class<R> targetClazz,String... ignoreProsNames){
        try {
            R newInstance = targetClazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source,newInstance,ignoreProsNames);
            return newInstance;
        }catch (Exception e){
            log.error("Bean Clone Error:{}",e.getMessage());
            return null;
        }
    }

    /**
     * @param sourceList      原实例集合
     * @param targetClazz     生成的新实例集合类型
     * @return java.util.List<V>
     * @apiNote 循环拷贝对象属性,并返回
     **/
    public static <S,R> List<R> copyValues(Collection<S> sourceList,Class<R> targetClazz){
        try {
            List<R> newInstances = new ArrayList<>(sourceList.size());
            for (S source : sourceList) {
                R newInstance = targetClazz.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source,newInstance);
                newInstances.add(newInstance);
            }
            return newInstances;
        }catch (Exception e){
            log.error("Batch Bean Clone Error:{}",e.getMessage());
            return new ArrayList<>();
        }
    }
}
