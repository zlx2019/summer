package com.zero.summer.core.utils.valid;

import java.util.Optional;

/**
 * Null值工具类
 * @author Zero.
 * @date 2021/10/25 2:41 下午
 */
public class NullUtil {

    /**
     * 断言并且赋值 target为Null 则source覆盖target
     * @param target    目标值
     * @param source    可取值
     * @param <T>       原型
     */
    public static<T> T isNullElse(T target, T source){
        AssertUtil.isNull(source,"source不能为空!");
        return Optional.ofNullable(target).orElse(source);
    }

}
