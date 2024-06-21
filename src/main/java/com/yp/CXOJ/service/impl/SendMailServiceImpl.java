package com.yp.CXOJ.service.impl;

import cn.hutool.core.util.StrUtil;
import com.yp.CXOJ.common.ErrorCode;
import com.yp.CXOJ.exception.ThrowUtils;
import com.yp.CXOJ.model.entity.User;
import com.yp.CXOJ.model.enums.UserRoleEnum;
import com.yp.CXOJ.service.SendMailService;
import com.yp.CXOJ.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.yp.CXOJ.constant.EmailConstant.EMAIL_MESSAGE;
import static com.yp.CXOJ.constant.EmailConstant.EMAIL_TITLE;
import static com.yp.CXOJ.service.impl.UserServiceImpl.SALT;

/**
 * @author 余炎培
 * @since 2024-06-16 星期日 12:42:37
 */
@Service
public class SendMailServiceImpl implements SendMailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${spring.mail.password}")
    private String password;

    @Resource
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param to       收件邮箱
     * @param authCode 验证码
     *                 return
     */
    @Override
    public Boolean sendVerificationCode(String to, String authCode) {
        try {
            // 创建邮箱对象
            SimpleEmail mail = new SimpleEmail();
            // 设置发送邮件的服务器
            mail.setHostName(host);
            // "你的邮箱号"+ "上文开启SMTP获得的授权码"
            mail.setAuthentication(from, password);
            // 发送邮件 "你的邮箱号"+"发送时用的昵称"
            mail.setFrom(from, "yyp");
            // 使用安全链接
            mail.setSSLOnConnect(true);
            // 接收用户的邮箱
            mail.addTo(to);
            // 邮件的主题(标题)
            mail.setSubject(EMAIL_TITLE);
            // 邮件的内容
            mail.setMsg(String.format(EMAIL_MESSAGE, "<h2 style='color: #FF7F50'>" + authCode + "</h2>"));
            // 发送
            mail.send();
        } catch (EmailException e) {
            ThrowUtils.throwIf(true, ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        return true;
    }

    @Override
    public User registerUser(String mail) {
        ThrowUtils.throwIf(StrUtil.isBlank(mail),
                ErrorCode.PARAMS_ERROR, "邮箱信息错误");
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + 12345678).getBytes());
        User user = new User();
        user.setUserAccount(mail.split("@")[1].split("\\.")[0] + mail.split("@")[0]);
        user.setUserPassword(encryptPassword);
        user.setUserName("方块人" + mail.split("@")[1].split("\\.")[0] + mail.split("@")[0]);
        user.setAddress(mail);
        user.setUserAvatar("https://img.zcool.cn/community/01460b57e4a6fa0000012e7ed75e83.png?x-oss-process=image/auto-orient,1/resize,m_lfit,w_1280,limit_1/sharpen,100");
        user.setUserProfile("这个人很神秘,什么都没有写");
        user.setUserRole(UserRoleEnum.USER.getValue());

        boolean save = userService.save(user);
        ThrowUtils.throwIf(!save, ErrorCode.OPERATION_ERROR, "邮箱用户注册失败");
        return user;
    }
}
