package com.zero.summer.core.utils.column;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 自定义VFunction接口 继承自Function、Serializable 获取序列化能力
 *
 * @author Zero.
 * @date 2022/3/19 6:41 下午
 */
@FunctionalInterface
public interface VFunction<T,R> extends Function<T,R>, Serializable {

}
