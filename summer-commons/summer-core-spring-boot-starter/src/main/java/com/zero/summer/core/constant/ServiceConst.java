package com.zero.summer.core.constant;

/**
 * 服务名常量
 * @author Zero.
 * @date 2023/2/14 5:06 PM
 */
public interface ServiceConst {

    /**
     * WebClient 服务通信使用的协议前缀
     */
    String PROTOCOL_PREFIX = "http://";

    /**
     * 实例服务名
     */
    String EXAMPLE = "summer-example-service";
    String EXAMPLE_URI = PROTOCOL_PREFIX + EXAMPLE;
    /**
     * 用户服务名
     */
    String USER = "summer-user-service";
    String USER_URI = PROTOCOL_PREFIX + USER;
}
