package com.zero.summer.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.UserResponse;
import com.zero.summer.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zero.
 * @date 2023/2/14 1:50 PM
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 注册用户
     * @param request 用户参数体
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@Validated @RequestBody SaveUserRequest request){
        return userService.save(request);
    }

    /**
     * 根据用户名获取用户信息
     * @param username  用户名
     * @return
     */
    @GetMapping("/find/user/username")
    public User findUserByUsername(String username){
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username);
        return userService.getOne(wrapper);
    }
}
