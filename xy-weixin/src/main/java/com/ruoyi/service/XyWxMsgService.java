package com.ruoyi.service;

import com.ruoyi.domain.vo.WeixinUserListVo;

/**
 * @author yuanjie
 * @date 2022/10/13
 */
public interface XyWxMsgService {
    /**
     * 新增微信用户到xy_user
     *
     * @return 结果
     */
    WeixinUserListVo insertWxUser();
}
