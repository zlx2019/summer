package com.zero.summer.core.utils.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.utils.JsonUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;

/**
 * 请求响应流工具类
 *
 * @author Zero.
 * @date 2023/3/2 11:06 AM
 */
public class ResponseUtil {
    private ResponseUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 通过流写到前端
     *
     * @param response     响应流
     * @param msg          返回信息
     * @param httpStatus   返回状态码
     */
    public static void responseWriter(HttpServletResponse response, String msg, int httpStatus) throws IOException {
        responseWrite(response, Result.Success(null,msg,httpStatus));
    }

    /**
     * 通过流写到前端
     * @param response     响应流
     * @param obj          响应数据
     */
    public static void responseSucceed(HttpServletResponse response, Object obj) throws IOException {
        responseWrite(response, Result.Success(obj));
    }

    /**
     * 将结果以流的方式响应给客户端
     * @param response     响应流
     * @param msg          响应消息
     */
    public static void responseFailed(ObjectMapper objectMapper, HttpServletResponse response, String msg) {
        responseWrite(response, Result.Success(msg));
    }


    /**
     * 将结果以流的方式响应给客户端
     * @param response     响应流
     * @param result       响应结果
     */
    private static void responseWrite(HttpServletResponse response, Result<?> result)  {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
        try {
            response.getWriter().write(JsonUtil.beanToJson(result));
            response.getWriter().flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
