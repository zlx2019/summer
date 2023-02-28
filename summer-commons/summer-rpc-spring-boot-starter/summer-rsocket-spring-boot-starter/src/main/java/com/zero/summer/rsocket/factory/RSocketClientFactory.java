package com.zero.summer.rsocket.factory;

import com.zero.summer.rsocket.client.ExampleSocketClient;
import com.zero.summer.rsocket.client.UserSocketClient;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.client.TcpClientTransport;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.service.RSocketServiceProxyFactory;

import java.time.Duration;

/**
 * RSocket 客户端工厂
 * @author Zero.
 * @date 2023/2/28 3:08 PM
 */
public class RSocketClientFactory {

    /**
     * RSocketRequester 构造器
     */
    @Autowired
    private RSocketRequester.Builder builder;
    /**
     * 请求者和响应程序组件使用的策略。
     */
    @Autowired
    private RSocketStrategies rSocketStrategies;

    /**
     * 初始化 统一设置客户端基础配置
     */
    @PostConstruct
    public void init(){
        // 设置组件使用的策略。
        builder.rsocketStrategies(rSocketStrategies);
        builder.rsocketConnector(connector -> {
            // 设置为零拷贝
           connector.payloadDecoder(PayloadDecoder.ZERO_COPY);
           // 拦截器设置 TODO 不生效
//           connector.interceptors(interceptorRegistry -> {
//              interceptorRegistry.forRequestsInRequester(rSocket -> new TestRequestInterceptor());
//              interceptorRegistry.forRequester(new RSocketInterceptor() {
//                  @Override
//                  public RSocket apply(RSocket rSocket) {
//                      return rSocket;
//                  }
//              });
//           });
        });
    }

    /**
     * 注入User服务RSocket客户端
     */
    @Bean
    public UserSocketClient userSocketClient(){
        // 指定User服务RSocket地址与端口
        return buildClient(TcpClientTransport.create("localhost", 12002), UserSocketClient.class);
    }


    /**
     * 注入Example服务RSocket客户端
     * @return {@link ExampleSocketClient}
     */
    @Bean
    public ExampleSocketClient exampleSocketClient(){
        // 指定Example服务RSocket地址与端口
        return buildClient(TcpClientTransport.create("127.0.0.1", 12001), ExampleSocketClient.class);
    }

    /**
     * 统一创建 不同模块的RSocket 客户端
     * @param clientTransport   要连接的服务端节点信息
     * @param clazz             客户端类型
     * @return                  客户端
     */
    private <T> T buildClient(TcpClientTransport clientTransport,Class<T> clazz){
        return RSocketServiceProxyFactory.builder(builder.transport(clientTransport))
                //配置等待具有同步（阻止）方法签名的 HTTP 服务方法响应的时间。
                //默认情况下，此值为 5 秒。
                .blockTimeout(Duration.ofSeconds(15))
                .build()
                // 创建RSocket客户端
                .createClient(clazz);
    }
}
