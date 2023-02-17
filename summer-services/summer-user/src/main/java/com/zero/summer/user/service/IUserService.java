package com.zero.summer.user.service;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.UserResponse;
import com.zero.summer.db.service.ISuperService;

/**
 * @author Zero.
 * @date 2023/2/17 12:19 AM
 */
public interface IUserService extends ISuperService<User> {


    Result<UserResponse> save(SaveUserRequest request);
}
