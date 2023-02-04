package com.zero.summer.core.constant;

/**
 * 常用正则表达式
 * @author Zero.
 * @date 2021/7/23 6:21 下午
 */
public interface MatchConst {

    /** 至少包含数字跟字母，可以有字符 6-20位*/
    String passwordStyle1 = "(?=.*([a-zA-Z].*))(?=.*[0-9].*)[a-zA-Z0-9-*/+.~!@#$%^&*()]{6,20}$";

    /** 手机号码正则表达式*/
    String MOBILE = "^1[3|4|5|8|7|9][0-9]\\d{8}$";

    /** 电子邮箱正则表达式*/
    String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
}
