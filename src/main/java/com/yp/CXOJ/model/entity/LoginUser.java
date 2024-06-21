package com.yp.CXOJ.model.entity;

import lombok.Data;

import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 余炎培
 * @since 2024-06-02 星期日 13:27:49
 */
@Data
public class LoginUser  implements Serializable {
    /**
     * 会话编号
     */
    private String sessionId;

    /**
     * id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 登录使用的浏览器
     */
    private String userAgent;

    /**
     * 登录ip
     */
    private String RemoteAddr;

    /**
     * 登录主机名
     */
    private String RemoteHost;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 登录时间
     */
    private Date loginTime;

    private static final long serialVersionUID = 1L;
}
