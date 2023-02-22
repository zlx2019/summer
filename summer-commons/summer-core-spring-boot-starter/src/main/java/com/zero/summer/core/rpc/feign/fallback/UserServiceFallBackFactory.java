package com.zero.summer.core.rpc.feign.fallback;

import com.zero.summer.core.entity.User;
import com.zero.summer.core.rpc.feign.UserService;
import feign.FeignException;
import feign.RetryableException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeoutException;

/**
 * UserService客户端降级工厂
 *
 * @author Zero.
 * @date 2023/2/21 9:03 PM
 */
@Component
@Slf4j
public class UserServiceFallBackFactory implements FallbackFactory<UserService> {

    /**
     * 返回导致异常错误的原因
     * @param cause 原因
     * @return fallback
     */
    @Override
    public UserService create(Throwable cause) {
        return new UserService() {
            @Override
            public User findUserByUsername(String username) {
                exceptionHandler("根据username获取用户信息异常",cause);
                return null;
            }

            /**
             * @param message 自定义业务日志消息
             * @param e 服务调用失败原因异常
             */
            private void exceptionHandler(String message ,Throwable e){
                String errMsg = "未知错误";
                // 服务不可用
                if (e instanceof FeignException.ServiceUnavailable){
                    errMsg = "服务不可用.";
                }else if (e instanceof TimeoutException|| e instanceof RetryableException){
                    errMsg = "服务连接超时.";
                }
                log.error("{} 原因:{}",message,errMsg,e);
            }
        };
    }
}
