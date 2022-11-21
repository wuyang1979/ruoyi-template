package com.ruoyi.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.domain.WxMsg;
import com.ruoyi.domain.WxMsgVO;

import java.util.List;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
public interface WxMsgService extends IService<WxMsg> {

    /**
     * 获取分组后的消息列表
     *
     * @param page
     * @param wxMsgVO
     * @return
     */
    IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, WxMsgVO wxMsgVO);
}
