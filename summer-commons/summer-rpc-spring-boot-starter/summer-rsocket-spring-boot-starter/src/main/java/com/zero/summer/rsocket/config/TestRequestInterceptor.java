package com.zero.summer.rsocket.config;

import io.netty.buffer.ByteBuf;
import io.rsocket.frame.FrameType;
import io.rsocket.plugins.RequestInterceptor;

/**
 * @author Zero.
 * @date 2023/2/28 9:21 PM
 */
public class TestRequestInterceptor implements RequestInterceptor {


    @Override
    public void onStart(int streamId, FrameType requestType, ByteBuf metadata) {
        System.out.println("OnStart");
    }

    @Override
    public void onTerminate(int streamId, FrameType requestType, Throwable t) {
        System.out.println("onTerminate");
    }

    @Override
    public void onCancel(int streamId, FrameType requestType) {
        System.out.println("onCancel");
    }

    @Override
    public void onReject(Throwable rejectionReason, FrameType requestType, ByteBuf metadata) {
        System.out.println("onReject");
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
    }
}
