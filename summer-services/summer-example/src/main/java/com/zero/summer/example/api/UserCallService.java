//package com.zero.summer.example.api;
//
//import com.zero.summer.core.entity.abstracts.Result;
//import org.springframework.http.MediaType;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.service.annotation.GetExchange;
//import org.springframework.web.service.annotation.HttpExchange;
//
//import java.util.List;
//
///**
// * @author Zero.
// * @date 2023/2/14 1:53 PM
// */
//@HttpExchange(value = "/user",contentType = MediaType.APPLICATION_JSON_VALUE,accept = MediaType.APPLICATION_JSON_VALUE)
//public interface UserCallService {
//
//    @GetExchange("/list")
//    Result<List<String>> list(@RequestParam("name") String name);
//
//}
