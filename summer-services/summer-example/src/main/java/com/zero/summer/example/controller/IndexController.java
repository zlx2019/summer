package com.zero.summer.example.controller;

import com.alibaba.nacos.api.exception.NacosException;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.lock.LockResult;
import com.zero.summer.lock.locks.EtcdDistributedLock;
import com.zero.summer.lock.locks.RedisDistributedLock;
import com.zero.summer.webclient.client.UserServiceClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Zero.
 * @date 2023/2/4 8:59 PM
 */
@RestController
@RequestMapping("index")
@Slf4j
public class IndexController {

    @Autowired
    private RedisDistributedLock redisDistributedLockl;
    @Autowired
    private EtcdDistributedLock etcdDistributedLock;


    @GetMapping("/hello")
    public Result<String> hello(String name,Integer age) throws NacosException {
        return Result.Success("Hello!");
    }

    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/test/call")
    public Result call(){
        Result<List<String>> result = userServiceClient.list("你好啊");
        return result;
    }

    @SneakyThrows
    @GetMapping("/test/lock")
    public Result lock(String key,long expire ){
        LockResult result = etcdDistributedLock.lock(key);
        TimeUnit.SECONDS.sleep(5);
        etcdDistributedLock.releaseLock(result);
        return Result.Success(result);
    }
}
