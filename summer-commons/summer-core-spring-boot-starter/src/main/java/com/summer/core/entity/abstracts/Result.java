package com.summer.core.entity.abstracts;

import com.summer.core.enums.ResultEnum;
import com.summer.core.constant.ResultConst;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 单数据 统一响应对象
 *
 * @author Zero.
 * @date 2022/1/23 11:23 上午
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 响应数据*/
    private T data;
    /** 响应消息*/
    private String message;
    /** 响应码 0:操作执行成功 1:操作执行异常*/
    private int code;

    /**
     * 业务结果是否成功
     */
    public  boolean isSuccess(){
        return this.code == ResultConst.SUCCESS_CODE;
    }

    /** 操作成功*/
    public static <T> Result<T> Success(T data, String message, int code){
        return new Result<>(data,message,code);
    }
    public static <T> Result<T> Success(T data,String message){
        return Result.Success(data,message, ResultConst.SUCCESS_CODE);
    }
    public static <T> Result<T> Success(T data){
        return Result.Success(data,ResultConst.SUCCESS);
    }
    public static <T> Result<T> Success(){
        return Result.Success(null);
    }

    /** 操作失败*/
    public static <T> Result<T> Failed(String message,int code){
        return new Result<>(null,message,code);
    }
    public static <T> Result<T> Failed(String message){
        return Result.Failed(message,ResultConst.FAILED_CODE);
    }
    public static <T> Result<T> Failed(){
        return Result.Failed(ResultConst.FAILED);
    }
    public static <T> Result<T> Failed(ResultEnum enums){
        return Failed(null,enums);
    }
    public static <T> Result<T> Failed(T data,ResultEnum enums){
        return new Result<>(data,enums.getMessage(),enums.getCode());
    }
}
