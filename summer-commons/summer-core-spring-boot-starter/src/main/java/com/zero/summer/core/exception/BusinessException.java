package com.zero.summer.core.exception;

import java.io.Serial;

/**
 * 自定义业务异常
 * @author Zero.
 * @date 2020/10/10
 */
public class BusinessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 6537761403724692400L;

    public BusinessException(String message) {
        super(message);
    }
    public BusinessException(Exception e){
        super(e);
    }

}
