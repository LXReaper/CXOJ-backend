package com.yp.CXOJ.model.dto.mail;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-17 星期一 00:24:56
 * 邮箱验证码登录请求类
 */
@Data
public class YZMLoginRequest implements Serializable {
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 验证码
     */
    private String mailYZM;


    private static final long serialVersionUID = 1L;
}
