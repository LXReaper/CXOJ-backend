package com.yp.CXOJ.service;

import com.yp.CXOJ.model.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 余炎培
 * @since 2024-06-16 星期日 12:42:18
 */
public interface SendMailService {


    /**
     * 发送验证码
     * @param to 收件邮箱
     * @param authCode 验证码
     * return
     */
    Boolean sendVerificationCode(String to,String authCode);


    /**
     * 注册一个用户
     * @param mail 邮箱
     * @return
     */
    User registerUser(String mail);
}
