package com.zero.summer.example.controller;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.holder.UserContextHolder;
import com.zero.summer.core.rpc.feign.UserService;
import com.zero.summer.core.rpc.web.UserServiceClient;
import com.zero.summer.rsocket.client.UserSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zero.
 * @date 2023/2/4 8:59 PM
 */
@RestController
@RequestMapping("index")
@Slf4j
public class IndexController {

//    @Autowired
//    private RedisDistributedLock redisDistributedLockl;
//    @Autowired
//    private EtcdDistributedLock etcdDistributedLock;

    @Autowired
    private UserService userService;
    @Autowired
    private UserSocketClient userSocketClient;
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/hello")
    public Result hello()  {
        log.info("userID:{}",UserContextHolder.getUser());
        User zlx = userServiceClient.findUserByUsername("zlx");
        return Result.Success(zlx);
    }

}
