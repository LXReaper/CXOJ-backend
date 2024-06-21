package com.yp.CXOJ.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.server.ServerEndpointConfig;

/**
 * @author 余炎培
 * @since 2024-06-09 星期日 14:51:13
 */
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws") //后端连接点
//                .setAllowedOrigins("*") //解决跨域问题
//                .withSockJS();
//    }
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        //topic 消息主题模式（发布/订阅模式   广播模式）
//        //queue 队列模式（点对点模式）
//        registry.enableSimpleBroker("/topic");//使用发布订阅模式
//        registry.setApplicationDestinationPrefixes("/app");
//    }
//}
@Configuration
public class WebSocketConfig{
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
