package com.zero.summer.auth.service;

import com.zero.summer.core.rpc.feign.UserService;
import com.zero.summer.core.rpc.web.UserServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Zero.
 * @date 2023/2/16 11:19 PM
 */
@Service
@RequiredArgsConstructor
public class IUserDetailsService implements UserDetailsService {

    private final UserServiceClient userServiceClient;
    private final UserService userService;

    /**
     * Secuity 认证获取处理方法,根据username查询用户信息
     * @param   username 用户名
     * @return  用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 远程调用用户服务,获取用户信息
        return userService.findUserByUsername(username);
    }
}
