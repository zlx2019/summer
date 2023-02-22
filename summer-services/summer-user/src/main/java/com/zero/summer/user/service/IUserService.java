package com.zero.summer.user.service;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.request.user.LoginUserRequest;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.user.UserResponse;
import com.zero.summer.db.service.ISuperService;

/**
 * @author Zero.
 * @date 2023/2/17 12:19 AM
 */
public interface IUserService extends ISuperService<User> {


    /**
     * 新增用户
     */
    Result<UserResponse> save(SaveUserRequest request);

    /**
     * 用户登录
     */
    Result<String> login(LoginUserRequest request);

    /**
     * 根据用户名获取用户信息
     * @param username  用户名
     */
    public User getUser(String username) ;
}
