package com.yp.CXOJ.model.dto.mail;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 余炎培
 * @since 2024-06-17 星期一 13:45:53
 * 绑定邮箱请求
 */
@Data
public class MailBindRequest implements Serializable {
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 验证码
     */
    private String mailYZM;

    /**
     * 用户id
     */
    private Long user_id;

    private static final long serialVersionUID = 1L;
}
