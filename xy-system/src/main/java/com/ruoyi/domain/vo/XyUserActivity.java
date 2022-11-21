package com.ruoyi.domain.vo;

import lombok.Data;

/**
 * @author yuanjie
 * @date 2022/10/27
 */
@Data
public class XyUserActivity {
    /**
     * 用户名
     */
    private String userName;

    /**
     * 登录时间
     */
    private String loginTime;

    public XyUserActivity() {
    }
}
