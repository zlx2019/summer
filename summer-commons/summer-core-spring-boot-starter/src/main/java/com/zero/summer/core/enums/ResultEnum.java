package com.zero.summer.core.enums;

import com.zero.summer.core.constant.ResultConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应结果枚举
 *
 * @author Zero.
 * @date 2022/3/25 11:33 上午
 */
@AllArgsConstructor
@Getter
public enum ResultEnum {
    /** Basic*/
    SUCCESS(ResultConst.SUCCESS_CODE,ResultConst.SUCCESS),//响应成功
    FAILED(ResultConst.FAILED_CODE,ResultConst.FAILED),//响应失败

    /** 认证鉴权*/
    AUTH_LOGIN_ERR(401,"认证失败!"),
    NOT_AUTHORIZED_ERR(403,"权限不足!"),
    USER_NOT_FOUND_ERR(4004,"该用户不存在!"),


    /** 参数*/
    VALID_DATA_ERR(400,"参数校验异常!");

    ;
    private final int code;
    private final String message;
}
