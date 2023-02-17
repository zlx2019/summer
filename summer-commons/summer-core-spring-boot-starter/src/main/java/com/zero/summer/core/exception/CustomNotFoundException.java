package com.zero.summer.core.exception;

import com.zero.summer.core.entity.abstracts.Result;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义404处理,响应Json格式
 *
 * @author Zero.
 * @date 2022/1/23 2:14 下午
 */
@Controller
public class CustomNotFoundException implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        //重新注册404异常handler,转发到自定义的处理控制器中
        registry.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND,"/music/notFound"));
    }

    /**
     * 自定义404异常响应
     */
    @GetMapping("/music/notFound")
    @ResponseBody
    public Result error404(){
        return Result.Failed("你迷路了吗~",404);
    }
}
