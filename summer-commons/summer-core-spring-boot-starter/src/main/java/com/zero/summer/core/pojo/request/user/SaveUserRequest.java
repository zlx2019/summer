package com.zero.summer.core.pojo.request.user;

import com.zero.summer.core.enums.Gender;


/**
 * 新增User请求参数体
 * @param username  用户名
 * @param nickname  用户昵称
 * @param password  密码
 * @param gender    性别
 */
public record SaveUserRequest(String username, String nickname, String password, Gender gender){ }
