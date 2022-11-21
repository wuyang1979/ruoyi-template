package com.ruoyi.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.domain.WxMsg;
import com.ruoyi.domain.WxMsgVO;
import com.ruoyi.mapper.WxMsgMapper;
import com.ruoyi.service.WxMsgService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
@Service
public class WxMsgServiceImpl extends ServiceImpl<WxMsgMapper, WxMsg> implements WxMsgService {

    @Override
    public IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, WxMsgVO wxMsgVO) {
        return baseMapper.listWxMsgMapGroup(page, wxMsgVO);
    }
}
