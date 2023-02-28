package com.zero.summer.core.rpc.web;

import com.zero.summer.core.constant.ServiceConst;
import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * 用户服务远程调用接口
 *
 * @author Zero.
 * @date 2023/2/14 1:53 PM
 *
 * url: http://目标服务名
 */
@HttpExchange(url = ServiceConst.USER_URI)
public interface UserServiceClient {

    /**
     * Example Get请求
     * @param name  参数必须使用@RequestParam("")注解指定参数名
     */
    @GetExchange("/user/list")
    Result<List<String>> list(@RequestParam("name") String name);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return         用户信息
     */
    @GetExchange("/user/{username}")
    User findUserByUsername(@PathVariable("username") String username);

}
