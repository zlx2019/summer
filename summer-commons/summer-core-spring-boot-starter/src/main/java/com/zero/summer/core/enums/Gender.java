package com.zero.summer.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别枚举
 *
 * @author Zero.
 * @date 2022/3/24 2:16 下午
 */
@Getter
@AllArgsConstructor
public enum Gender {


    UNKNOWN(0,"未知"),
    MALE(1,"男"),
    FEMALE(2,"女");

    /**
     * 通过{@link EnumValue}注解, MybatisPlus 插入数据库时默认用code值
     */
    @EnumValue
    private final Integer code;

    /**
     * 映射为Json时 使用value值
     */
    @JsonValue
    private final String value;

}
