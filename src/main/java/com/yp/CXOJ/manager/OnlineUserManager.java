package com.yp.CXOJ.manager;

import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.entity.LoginUser;
import com.yp.CXOJ.model.entity.User;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 余炎培
 * @since 2024-06-02 星期日 12:24:48
 */
public class OnlineUserManager {
    //获取在线人数
    //创建数据类型set来存储session
    //这里不用会话id来作为键是因为不同浏览器的会话id不同，当用户在不同浏览器登录同一账号时，onlineUsers中存的该账号信息只有一个，则键值只能使用用户id
    @Getter
    private static final Map<Long, LoginUser> onlineUsers = new ConcurrentHashMap<>();

    @Getter
    private static final Map<Long, HttpSession> sessions = new ConcurrentHashMap<>();

    //添加访问
    public static void addUser(LoginUser user, HttpServletRequest request) {
        onlineUsers.put(user.getId(),user);
        sessions.put(user.getId(),request.getSession());
    }

    //删除目标id的user
    public static HttpSession removeUser(Long id) {
        onlineUsers.remove(id);
        return sessions.remove(id);
    }
}
