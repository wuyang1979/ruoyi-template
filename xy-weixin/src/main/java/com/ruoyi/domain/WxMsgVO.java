package com.ruoyi.domain;

import lombok.Data;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
@Data
public class WxMsgVO extends WxMsg {
    private static final long serialVersionUID = 1L;

    /**
     * 数量
     */
    private Integer countMsg;

    /**
     * repType not in筛选
     */
    private String notInRepType;

}
