package com.zero.summer.webclient.service;

import com.zero.summer.core.entity.abstracts.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * 用户服务远程调用接口
 *
 * @author Zero.
 * @date 2023/2/14 1:53 PM
 */
@HttpExchange(
        contentType = MediaType.APPLICATION_JSON_VALUE,
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface UserCallService {

    /**
     * Example Get请求
     * @param name  参数必须使用@RequestParam("")注解指定参数名
     */
    @GetExchange("/user/list")
    Result<List<String>> list(@RequestParam("name") String name);

}
