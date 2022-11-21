package com.ruoyi.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ThirdSession implements Serializable {
    /**
     * 微信用户ID
     */
    private String wxUserId;
    /**
     * 配置项ID
     */
    private String appId;
    /**
     * 微信sessionKey
     */
    private String sessionKey;
    /**
     * 用户标识
     */
    private String openId;
}
