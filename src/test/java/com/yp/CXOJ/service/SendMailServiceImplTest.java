package com.yp.CXOJ.service;

import com.yp.CXOJ.constant.EmailConstant;
import org.junit.jupiter.api.Test;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import static org.junit.jupiter.api.Assertions.*;

class SendMailServiceImplTest {

    @Test
    public void sendVerificationCode() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("3773112670@qq.com");
        message.setTo("1326126225@qq.com");
        message.setSubject(EmailConstant.EMAIL_SUBJECT);
        message.setText(EmailConstant.EMAIL_TITLE);

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        try {
            //发送邮件
            mailSender.send(message);
            System.out.println("纯文本邮件发送成功");
        }catch (MailException e) {
            System.out.println("纯文本邮件发送失败"+e.getMessage());
            e.printStackTrace();
        }
    }
}