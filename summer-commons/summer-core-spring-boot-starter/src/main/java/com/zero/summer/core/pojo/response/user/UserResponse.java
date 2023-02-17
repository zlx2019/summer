package com.zero.summer.core.pojo.response.user;

import com.zero.summer.core.enums.Gender;


/**
 * User响应视图实体
 * @param id        用户ID
 * @param username 用户名
 * @param nickname  用户昵称
 * @param gender    性别
 */
public record UserResponse(Long id, String username, String nickname, Gender gender) {

}
