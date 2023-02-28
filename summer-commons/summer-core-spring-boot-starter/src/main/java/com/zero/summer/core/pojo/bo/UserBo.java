package com.zero.summer.core.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * User 服务调用实体
 *
 * @author Zero.
 * @date 2023/2/28 4:26 PM
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String userName;
}
