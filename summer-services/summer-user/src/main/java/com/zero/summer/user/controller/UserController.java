package com.zero.summer.user.controller;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.holder.UserContextHolder;
import com.zero.summer.core.pojo.request.user.LoginUserRequest;
import com.zero.summer.core.pojo.request.user.SaveUserRequest;
import com.zero.summer.core.pojo.response.user.UserResponse;
import com.zero.summer.user.service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zero.
 * @date 2023/2/14 1:50 PM
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final IUserService userService;
    /**
     * 注册用户
     * @param request 用户参数体
     */
    @PostMapping("/register")
    public Result<UserResponse> register(@RequestBody SaveUserRequest request){
        return userService.save(request);
    }

    /**
     * 用户登录认证,获取Token令牌
     * @param request 登录参数
     * @return  token
     */
    @GetMapping("/login")
    public Result<String> login(@RequestBody LoginUserRequest request) throws InterruptedException {
        return userService.login(request);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username  用户名
     * @return {@link User}
     */
    @GetMapping("/{username}")
    public User findUserByUsername(@PathVariable("username") String username, HttpServletRequest request) throws InterruptedException {
        log.info("UserID:{}", UserContextHolder.getUser());
//        log.info("userID:{}",UserContextHolder.getUser());
//        log.info("token:{}",request.getHeader(SecurityConstant.TOKEN_KEY));
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User u = (User)authentication.getPrincipal();
//        log.info("username:{}",u.getUsername());
//        TimeUnit.SECONDS.sleep(10);
        return userService.getUser(username);
    }

}
