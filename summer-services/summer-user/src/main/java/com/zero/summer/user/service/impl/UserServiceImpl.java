package com.zero.summer.user.service.impl;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.UserResponse;
import com.zero.summer.core.utils.CloneUtil;
import com.zero.summer.db.service.impl.SuperServiceImpl;
import com.zero.summer.user.mapper.UserMapper;
import com.zero.summer.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Zero.
 * @date 2023/2/17 12:20 AM
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements IUserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Result<UserResponse> save(SaveUserRequest request) {
        User user = CloneUtil.copyValue(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean save = save(user);
        UserResponse userResponse = new UserResponse(user.getId(),user.getUsername(),user.getNickname(),user.getGender());
        return Result.Success(userResponse);
    }
}
