package com.zero.summer.core.constant;

/**
 * 常量
 *
 * @author Zero.
 * @date 2022/3/24 7:28 下午
 */
public interface Constant {

    /** 默认页码、页容量 */
    Integer DEFAULT_PAGE = 1;
    Integer DEFAULT_PAGE_SIZE = 10;
    String PAGE_MIN_MESSAGE = "页码最小只能为1!";


    /** 日期格式化*/
    String TIME = "HH:mm:ss";
    String MONTH_FORMAT = "yyyy-MM";
    String DATE_FORMAT = "yyyy-MM-dd";
    String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    String DATE_NO_FORMAT = "yyyyMMddHHmmss";
    String WECHAT_DATEFORMAT = "yyyy-MM-dd'T'HH:mm:ss+08:00";

    /** 默认字符集格式*/
    String CHAR_SET = "UTF-8";
    /** UserId 在Token中的Key*/
    String TOKEN_USER_ID = "userId";
}
