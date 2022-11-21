package com.ruoyi.common.core.domain.model;

import lombok.Data;

/**
 * 用户登录对象
 *
 * @author ruoyi
 */
@Data
public class LoginBody {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 验证码
     */
    private String code;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 手机验证码
     */
    private String smsCode;
}
