package com.suzu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /*
    * message broker 消息代理
    * */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
//        simpleBroker 就是一个基于内存的消息代理
//        这里的 destinationPrefixes 代表的是将消息代理携带的内容返回 /topic 下的位置
        config.enableSimpleBroker("/topic");
//        这是定义消息映射 指定了在哪个 url 下处理请求信息
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000")
                .withSockJS();
    }
}
