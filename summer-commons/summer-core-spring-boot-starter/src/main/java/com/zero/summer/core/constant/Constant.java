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


    /**
     * 用户id 在请求头中的Key
     */
    String USER_ID_HEADER = "x-userId-header";

    /**
     * 用户id 在tokenClaims中的Key
     */
    String USER_ID_TOKEN = "userId-token";

    /**
     * 日志链路追踪id信息头
     */
    String TRACE_ID_HEADER = "x-traceId-header";
    /**
     * 日志链路追踪id日志标志
     */
    String LOG_TRACE_ID = "traceId";

    /**
     * RPC通信时 用户ID标识Key
     */
    String RPC_USER_ID_KEY = "rpc-user-key";

    /**
     * RPC通信时,服务端响应码Key
     */
    String RPC_RESULT_CODE_KEY = "rpc-result-code-key";

}
