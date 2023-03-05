package com.zero.summer.core.config;

import cn.hutool.core.util.StrUtil;
import com.zero.summer.core.entity.abstracts.Result;
import com.zero.summer.core.enums.ResultEnum;
import com.zero.summer.core.exception.BusinessException;
import com.zero.summer.core.exception.CustomNotFoundException;
import com.zero.summer.core.utils.text.TextUtil;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * 全局异常捕获处理
 *
 * @author Zero.
 * @date 2022/3/24 7:36 下午
 */
@Slf4j
@ResponseBody
@Import(CustomNotFoundException.class)
@ControllerAdvice
public class DefaultExceptionConfigurer {
    /**
     * BusinessException 自定义业务异常处理
     * 返回状态码:200
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleException(BusinessException e) {
        return defHandler(e.getMessage() == null ? "业务执行异常,请检查!" : e.getMessage(), e);
    }


    /**
     * 所有异常统一处理
     * 返回状态码:500
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        return defHandler("系统异常,请联系业务崽!", e);
    }

    /**
     * Http请求类型错误异常
     * 返回状态码:405
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return defHandler("不支持当前请求方法", e);
    }

    /**
     * 如果身份验证请求因凭据无效而被拒绝，则引发。对于要引发的异常，这意味着帐户既未锁定也未禁用
     * 安全认证失败异常处理
     * 返回状态码:401
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(BadCredentialsException.class)
    public Result handleAuthLoginException(BadCredentialsException e){
        return defHandler("登录失败,用户名密码不正确!",e);
    }


    /**
     * gRPC 通信异常处理
     * @param e grpc响应的异常
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(StatusRuntimeException.class)
    public Result<?> gRpcHandleExecution(StatusRuntimeException e){
        // 将异常转换为gRPC的Status的Code,根据该Code区分异常类型
        Status status = Status.fromThrowable(e);
        Status.Code code = status.getCode();
        String errorMsg = switch (code){
            case UNAVAILABLE -> "服务节点不可用,请确认服务已启动~";
            case UNAUTHENTICATED -> "访问权限不足~";
            default -> "未知错误~";
        };
        String s = TextUtil.format("gRPC服务调用失败 Code: {} Message: {} Err:{}", code, errorMsg, status.getDescription());
        log.error(s);
        return defHandler(errorMsg,e);
    }

    /**
     * 缺少请求体异常
     * 返回状态码:411
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.LENGTH_REQUIRED)
    public Result handleRequestBodyException(HttpMessageNotReadableException e){
        return defHandler("缺少请求体,请用application/json方式请求",e);
    }

    /**
     * IllegalArgumentException异常处理返回json
     * 返回状态码:400
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public Result badRequestException(IllegalArgumentException e) {
        return defHandler("参数解析失败", e);
    }

    /**
     * 参数必填项异常
     * 返回状态码:400
     *
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class})
    public Result paramsNotNullException(MissingServletRequestParameterException e){
        return defHandler(StrUtil.format("参数{}为必填项",e.getParameterName()),e);
    }


    /**
     * 参数校验错误异常
     *
     * @param e MethodArgumentNotValidException
     * @return  Map<field,errorMessage>
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Result handleException(MethodArgumentNotValidException e) {
        Map<String, String> errorMap = new LinkedHashMap<>();
        List<ObjectError> messages = e.getBindingResult().getAllErrors();
        List<FieldError> fields = e.getBindingResult().getFieldErrors();
        IntStream.range(0,messages.size()).forEach(i-> {
            String message = messages.get(i).getDefaultMessage();
            String field = fields.get(i).getField();
            errorMap.put(field,message);
        });
        return Result.Failed(errorMap, ResultEnum.VALID_DATA_ERR);
    }


    /**
     * 用户不存在异常
     * UsernameNotFoundException
     * 返回状态码:4004
     */
    @ExceptionHandler({UsernameNotFoundException.class})
    public Result badMethodExpressException(UsernameNotFoundException e) {
        return defHandler(ResultEnum.USER_NOT_FOUND_ERR, e);
    }

    /**
     * 权限不足异常捕获
     * AccessDeniedException异常处理返回json
     * 返回状态码:403
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({AccessDeniedException.class})
    public Result badMethodExpressException(AccessDeniedException e) {
        return defHandler(ResultEnum.NOT_AUTHORIZED_ERR.getMessage(), e);
    }


    private Result defHandler(ResultEnum enums, Exception e) {
        return defHandler(enums.getMessage(),e);
    }
    /**
     * 记录异常日志 响应客户端
     *
     * @param message   异常消息
     * @param e         异常
     * @return          异常统一响应
     */
    private Result defHandler(String message, Exception e) {
        log.error(message,e);
        return Result.Failed(message);
    }

}
