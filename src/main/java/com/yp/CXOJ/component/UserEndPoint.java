package com.yp.CXOJ.component;

import com.yp.CXOJ.config.GetHttpSessionConfig;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.model.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 余炎培
 * @since 2024-06-09 星期日 20:00:22
 */
@ServerEndpoint(value = "/user",configurator = GetHttpSessionConfig.class)
@Component
public class UserEndPoint {
    //线程安全的Map
    public static final Map<String,Session> onlineUsers = new ConcurrentHashMap<>();

    //保存当前的会话信息
    private HttpSession httpSession;

    /**
     * webSocket建立连接时的方法
     * @param session
     * @param config
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config) {
        //将httpSession进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        User user = (User) httpSession.getAttribute(UserConstant.USER_LOGIN_STATE);
        onlineUsers.put(user.getId().toString(), session);//使用User的id来存放session

        //广播消息



    }

    //接收浏览器发送的Message
    @OnMessage
    public void onMessage(String message) {}

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {}

    @OnError
    public void onError(Session session, Throwable throwable) {}
}
