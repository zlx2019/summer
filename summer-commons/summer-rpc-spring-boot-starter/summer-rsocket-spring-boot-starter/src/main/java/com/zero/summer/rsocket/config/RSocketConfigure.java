package com.zero.summer.rsocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.codec.cbor.Jackson2CborDecoder;
import org.springframework.http.codec.cbor.Jackson2CborEncoder;
import org.springframework.messaging.rsocket.DefaultMetadataExtractor;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.web.util.pattern.PathPatternRouteMatcher;

/**
 * RSocket 全局配置
 * @author Zero.
 * @date 2023/2/28 2:42 PM
 */
public class RSocketConfigure {

    /**
     * 扩展 MessageMappingMessageHandler 用于处理 和 方法的 @ConnectMapping @MessageMapping RSocket 请求。
     * @return {@link RSocketMessageHandler}
     */
    @Bean
    public RSocketMessageHandler rSocketMessageHandler(RSocketStrategies strategies){
        RSocketMessageHandler handler = new RSocketMessageHandler();
        // 设置请求响应处理策略
        handler.setRSocketStrategies(strategies);
        // 设置数据负载的默认内容类型
        handler.setDefaultDataMimeType(MediaType.APPLICATION_JSON);
        // 设置元数据默认类型
        // 默认情况下，此设置为 "message/x.rsocket.composite-metadata.v0"
        handler.setDefaultMetadataMimeType(MediaType.APPLICATION_JSON);
        // 设置请求元数据提取器,用于提取 RSocket 请求中的元数据，并将其转换为一个 Map
        handler.setMetadataExtractor(new DefaultMetadataExtractor());
        return handler;
    }


    /**
     * 配置 RSocket 请求者和响应程序组件使用的策略。
     * @return {@link RSocketStrategies}
     */
    @Bean
    public RSocketStrategies rSocketStrategies(){
        return RSocketStrategies.builder()
                // 指定编码器
                .encoders(encoders -> encoders.add(new Jackson2CborEncoder()))
                // 指定解码器
                .decoders(decoders -> decoders.add(new Jackson2CborDecoder()))
                // 指定路由处理器
                .routeMatcher(new PathPatternRouteMatcher())
//                .dataBufferFactory(new NettyDataBufferFactory(ByteBufAllocator.DEFAULT))
                .build();
    }

}
