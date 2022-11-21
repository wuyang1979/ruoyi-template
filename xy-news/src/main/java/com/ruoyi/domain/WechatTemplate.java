package com.ruoyi.domain;

import com.ruoyi.domain.vo.TemplateData;
import lombok.Data;

import java.util.Map;
import java.util.TreeMap;

@Data
public class WechatTemplate {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板id
     */
    private String template_id;

    /**
     * 模板跳转链接(海外账号没有跳转能力)
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private TreeMap<String, String> miniprogram;

    /**
     * 模板数据
     */
    private Map<String, TemplateData> data;
}
