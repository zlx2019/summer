package com.zero.summer.core.enums;

import com.zero.summer.core.constant.ServiceConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务名枚举
 * @author Zero.
 * @date 2023/2/14 5:08 PM
 */
@Getter
@AllArgsConstructor
public enum ServiceInstance {
    USER(ServiceConst.USER,"用户服务"),
    EXAMPLE(ServiceConst.EXAMPLE,"示例服务"),;

    /**
     * 服务名
     */
    private String name;
    /**
     * 服务描述
     */
    private String message;
}
