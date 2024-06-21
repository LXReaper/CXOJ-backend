package com.yp.CXOJ.listeners;

import com.yp.CXOJ.manager.OnlineUserManager;
import com.yp.CXOJ.model.entity.LoginUser;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @author 余炎培
 * @since 2024-06-02 星期日 13:52:16
 */

@WebListener
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent event) {
        // 会话创建时执行的操作
    }

    //todo 这里可能会有同时结束session的情况，此时需要考虑其他方式
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        // 会话销毁时执行的操作
//        for (LoginUser loginUser : OnlineUserManager.getOnlineUsers()) {
//            if (loginUser.getSessionId().equals(session.getId())) {
//                OnlineUserManager.getOnlineUsers().remove(loginUser);
//            }
//        }
    }
}
