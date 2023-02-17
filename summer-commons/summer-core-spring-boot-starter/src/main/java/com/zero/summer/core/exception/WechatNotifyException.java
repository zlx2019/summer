package com.zero.summer.core.exception;

/**
 * 自定义异常-微信异步回调通知异常
 *
 * @author Zero.
 * @date 2022/6/27 1:23 PM
 */
public class WechatNotifyException extends PaymentException {

    public WechatNotifyException(String message){
        super(message);
    }
}
