package com.zero.summer.core.exception;

/**
 * 自定义支付系列异常
 *
 * @author Zero.
 * @date 2022/6/28 2:26 PM
 */
public class PaymentException extends RuntimeException {
    public PaymentException(String message){
        super(message);
    }
    public PaymentException(Exception e){
        super(e);
    }
}
