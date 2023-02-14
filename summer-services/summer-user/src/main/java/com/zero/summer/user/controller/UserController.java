package com.zero.summer.user.controller;

import com.zero.summer.core.entity.abstracts.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Zero.
 * @date 2023/2/14 1:50 PM
 */
@RestController
@RequestMapping("user")
public class UserController {


    @GetMapping("/list")
    public Result<List<String>> list(@RequestParam String name) throws InterruptedException {
        return Result.Success(Arrays.asList("Zero","满城雪","海问香"));
    }

    @GetMapping("/hello")
    public String hello(){
        return "123";
    }

}
