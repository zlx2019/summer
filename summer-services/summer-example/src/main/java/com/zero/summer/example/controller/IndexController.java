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
//    @Autowired
//    private LoadBalancerClient loadBalancerClient;


    @GetMapping("/hello")
    public Result<String> hello(String name,Integer age) throws NacosException {
//        ServiceInstance instance2 = loadBalancerClient.choose("summer-example-service");

//        log.info("instance {}",instance2.getUri());
        return Result.Success("Hello!");
    }

//    @Autowired
//    private ReactorLoadBalancerExchangeFilterFunction lbFunction;
    @Autowired
    private UserServiceClient userServiceClient;

    @GetMapping("/test/call")
    public Result call(){
//        WebClient client = WebClient.builder()
////                .baseUrl("http://127.0.0.1:8888")
//                .baseUrl("http://summer-user-service")
//                .filter(lbFunction)
//                .build();
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
//        UserCallService service = factory.createClient(UserCallService.class);
//        Result<List<String>> result = service.list("Hello");
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
