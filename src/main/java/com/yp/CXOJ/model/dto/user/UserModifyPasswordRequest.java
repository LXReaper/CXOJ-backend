package com.yp.CXOJ.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户修改密码请求
 *
 */
@Data
public class UserModifyPasswordRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    private String userAccount;

    private String oldPassword;

    private String newPassword;
}
