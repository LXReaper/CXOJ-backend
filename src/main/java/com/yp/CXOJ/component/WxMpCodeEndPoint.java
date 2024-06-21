package com.yp.CXOJ.component;

import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.config.GetHttpSessionConfig;
import com.yp.CXOJ.constant.UserConstant;
import com.yp.CXOJ.exception.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
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
 * @since 2024-06-09 星期日 22:07:56
 * 微信扫码登录的websocket服务
 */
@ServerEndpoint(value = "/wxMpCode",configurator = GetHttpSessionConfig.class)
@Component
@Slf4j
public class WxMpCodeEndPoint{
    public static final Map<String, Session> loginWxUsers = new ConcurrentHashMap<>();

    //保存当前的会话信息
    private HttpSession httpSession;

    /**
     * webSocket建立连接时的方法
     * @param session
     * @param config
     */
    @OnOpen
    public synchronized void onOpen(Session session, EndpointConfig config) {
        //将httpSession进行保存
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        loginWxUsers.put(httpSession.getId(), session);//使用User的id来存放session
        log.info("[微信扫码连接] 当前websocket的连接数 {}",loginWxUsers.size());
        //广播消息
    }

    //接收浏览器发送的Message
    @OnMessage
    public void onMessage(String message, Session session) {}

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        loginWxUsers.remove(httpSession.getId());
        log.info("[微信扫码连接] 当前websocket的连接数 {}",loginWxUsers.size());
        log.info("[微信扫码连接] 已关闭 {}",closeReason.getReasonPhrase());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("[微信扫码连接] websocket连接错误 {}",throwable.getMessage());
    }


    /**
     * 发送消息
     * @param message
     */
    public void sendToAllClient(String message,String state) {
        //获取到当前的会话
        for (Map.Entry<String, Session> loginWxUser : loginWxUsers.entrySet()) {
            ThrowUtils.throwIf(loginWxUser == null , ErrorCode.SYSTEM_ERROR,"会话不存在");
            if (state.equals(loginWxUser.getKey())){
                loginWxUser.getValue().getAsyncRemote().sendText(message);
                break;
            }
        }
//        for (Session currentSession : loginWxUsers.values()) {
//            ThrowUtils.throwIf(currentSession == null , ErrorCode.SYSTEM_ERROR,"会话不存在");
//            //getBasicRemote同步，getAsyncRemote异步
//            currentSession.getAsyncRemote().sendText(message);
//        }
    }
}
