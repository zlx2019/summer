package com.zero.summer.core.rpc.feign;

import com.zero.summer.core.constant.ServiceConst;
import com.zero.summer.core.entity.User;
import com.zero.summer.core.rpc.feign.fallback.UserServiceFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * User Feign客户端
 * 参数详解:
 * contextId: beanID,防止多个服务端重名
 * name: 要调用的服务的服务名
 * url: 服务的url(可选的)
 * dismiss404: 作用是在服务调用返回HTTP 404错误时，告诉FeignClient不要抛出异常而是返回null。这样，可以让调用者不需要关心404错误，只关心正常的返回结果。默认为false
 * configuration: 给本客户端指定自定义配置,而不使用全局配置. 也可以使用默认配置{@link org.springframework.cloud.openfeign.FeignClientsConfiguration}
 * fallbackFactory: 降级工厂
 * path: 所有请求的前缀
 * primary: 关闭设置为默认实例。将 Feign 与 Hystrix 回退一起使用时，同一类型中有多个 bean 。
 *
 * @author Zero.
 * @date 2023/2/21 8:49 PM
 */
@FeignClient(name = ServiceConst.USER,dismiss404 = false,primary = false,fallbackFactory = UserServiceFallBackFactory.class)
public interface UserService {

    /**
     * 根据用户名 获取用户信息{@link User} (服务调用)
     * @param username  用户名
     * @return          用户信息
     */
    @GetMapping("/user/{username}")
    User findUserByUsername(@PathVariable("username") String username);
}
