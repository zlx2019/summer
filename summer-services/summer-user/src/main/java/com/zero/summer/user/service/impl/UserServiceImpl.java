package com.zero.summer.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.request.user.LoginUserRequest;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.user.UserResponse;
import com.zero.summer.core.utils.CloneUtil;
import com.zero.summer.core.utils.TokenUtil;
import com.zero.summer.db.service.impl.SuperServiceImpl;
import com.zero.summer.user.mapper.UserMapper;
import com.zero.summer.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    /**
     * 身份验证管理器
     * 通过管理器来认证用户信息
     */
    private final AuthenticationManager authenticationManager;

    /**
     * 新增用户
     * @param request 参数体
     * @return {@link UserResponse}
     */
    @Override
    public Result<UserResponse> save(SaveUserRequest request) {
        User user = CloneUtil.copyValue(request, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        boolean save = save(user);
        UserResponse userResponse = new UserResponse(user.getId(),user.getUsername(),user.getNickname(),user.getGender());
        return Result.Success(userResponse);
    }

    /**
     * 用户登录
     *
     * @param request
     */
    @Override
    public Result<String> login(LoginUserRequest request) {
        // 构建成要被认证的信息
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.username(), request.password()
        );
        // 通过身份验证管理器 校验用户名密码是否正确
        // 自动通过 IUserDetailsService.loadUserByUsername()方法校验
        // 校验失败则会抛出异常
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 强转为自定义User对象,生成Token
        User user = (User) authenticate.getPrincipal();
        return Result.Success(TokenUtil.generateToken(user));
    }

    /**
     * 根据用户名获取用户信息
     * @param username  用户名
     */
    @Override
    public User getUser(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        return this.getOne(wrapper);
    }
}
