package com.zero.summer.example.controller;

import com.zero.summer.core.entity.abstracts.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zero.
 * @date 2023/2/4 8:59 PM
 */
@RestController
@RequestMapping("index")
public class IndexController {


    @GetMapping("/hello")
    public Result<String> hello(String name,Integer age){
        return Result.Success("Hello!");
    }
}
