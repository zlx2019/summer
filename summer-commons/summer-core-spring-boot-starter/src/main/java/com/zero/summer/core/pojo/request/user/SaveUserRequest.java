package com.zero.summer.core.pojo.request.user;

import com.zero.summer.core.enums.Gender;


/**
 * 新增User请求参数体
 * @param username
 * @param nickname
 * @param password
 * @param gender
 */
public record SaveUserRequest(String username, String nickname, String password, Gender gender){ }
