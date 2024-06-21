package com.yp.CXOJ.constant;

/**
 * @author 余炎培
 * @since 2024-06-16 星期日 12:49:06
 */
public interface EmailConstant {

    /**
     * 电子邮件主题
     */
    String EMAIL_SUBJECT = "验证码邮件";


    /**
     * 邮件标题
     */
    String EMAIL_TITLE = "CXOJ用户邮箱验证";

    /**
     * 邮件内容（授权注册）
     */
    String EMAIL_MESSAGE = "<h1>亲爱的CXOJ用户：您好！</h1>\n您的验证码为:" + "%s" + "(一分钟内有效)";

}
