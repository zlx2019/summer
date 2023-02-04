package com.zero.summer.core.utils.valid;


import com.zero.summer.core.constant.MatchConst;
import com.zero.summer.core.constant.ResultConst;
import com.zero.summer.core.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 断言工具类
 *
 * @author Zero.
 * @date 2022/1/9 3:14 下午
 */
public class AssertUtil {
    static final String TEXT_ERR = "多文本断言未通过,不允许有空值,请检查程序!";
    static final String OBJ_ERR = "多对象断言未通过,不允许有Null,请检查程序!";
    static final String BOOL_ERR = "多boolean值断言未通过,请检查程序";

    /**
     * 断言: 字符串为空 抛出异常
     *
     * @param obj     目标参数
     * @param message 异常消息
     */
    public static void isBlank(String obj, String... message) {
        if (StringUtils.isBlank(obj)) {
            execute(message);
        }
    }


    /**
     * 断言: 多字符串断言,若有其一为空值 抛出异常
     *
     * @param objects 参数列表
     */
    public static void isBlanks(String... objects) {
        Arrays.stream(objects).forEach(str -> {
            if (StringUtils.isBlank(str)) {
                execute(TEXT_ERR);
            }
        });
    }

    /**
     * 断言: 两个字符串不一致 抛出异常
     *
     * @param str1    文本1
     * @param str2    文本2
     * @param message 异常消息
     */
    public static void isEquals(String str1, String str2, String... message) {
        if (!StringUtils.equals(str1, str2)) {
            execute(message);
        }
    }

    /**
     * 断言: 对象为Null 抛出异常
     *
     * @param obj     目标对象
     * @param message 异常消息
     */
    public static void isNull(Object obj, String... message) {
        if (null == obj) {
            execute(message);
        }
    }


    /**
     * 断言: 多对象断言,有一个为Null,则抛出异常
     *
     * @param objects 对象列表
     */
    public static void isNulls(Object... objects) {
        Arrays.stream(objects).forEach(obj -> {
            if (null == obj) {
                execute(OBJ_ERR);
            }
        });
    }


    /**
     * 断言: 参数为True 抛出异常
     *
     * @param obj     目标参数
     * @param message 异常消息
     */
    public static void isTrue(boolean obj, String... message) {
        if (obj) {
            execute(message);
        }
    }


    /**
     * 断言: 参数为False 抛出异常
     *
     * @param obj     目标参数
     * @param message 异常消息
     */
    public static void isFalse(boolean obj, String... message) {
        if (!obj) {
            execute(message);
        }
    }

    /**
     * 断言: 多bool值断言,有一个为false 则抛出异常
     *
     * @param objects 参数列表
     */
    public static void isFalse(Boolean... objects) {
        if (!Arrays.stream(objects).allMatch(Boolean::booleanValue)) {
            execute(BOOL_ERR);
        }
    }

    /**
     * 断言: 手机号码格式是否正确
     *
     * @param mobile 手机号码
     */
    public static void mobileValid(String mobile) {
        isBlank(mobile, "手机号码不能为空~");
        isTrue(mobile.matches(MatchConst.MOBILE), "手机号码格式不正确~");
    }

    /**
     * 校验: 电子邮箱格式是否正确
     *
     * @param email 电子邮箱
     */
    public static void emailValid(String email) {
        isBlank(email, "邮箱号码不能为空~");
        isTrue(email.matches(MatchConst.EMAIL), "电子邮箱格式不正确~");
    }

    /**
     * 数值最小校验
     *
     * @param number    数值对象
     * @param message   提示消息
     */
    public static void isMin(Integer number,Integer min,String message){
        isTrue(number < min,message);
    }

    /**
     * 统一异常处理
     *
     * @param message 异常提示信息
     */
    private static void execute(String... message) {
        String msg = ResultConst.FAILED;
        if (message != null && message.length > 0) {
            msg = message[0];
        }
        throw new BusinessException(msg);
    }
}
