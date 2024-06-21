package com.yp.CXOJ.component;

import com.yp.CXOJ.config.GetHttpSessionConfig;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 余炎培
 * @since 2024-06-09 星期日 20:38:09
 */
@ServerEndpoint(value = "/comment",configurator = GetHttpSessionConfig.class)
@Component
public class CommentEndPoint {
    public static final Map<String,Session> onlineUsers = new ConcurrentHashMap<>();

    //保存当前的会话信息
    private HttpSession httpSession;

    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //将httpSession进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());

    }

    @OnMessage
    public void onMessage(String message) {}

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {}

    @OnError
    public void onError(Session session, Throwable throwable) {}
}
