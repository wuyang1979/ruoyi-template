package com.ruoyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.domain.WxMsg;
import com.ruoyi.domain.WxMsgVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 微信消息
 *
 * @author www.joolun.com
 * @date 2019-05-28 16:12:10
 */
public interface WxMsgMapper extends BaseMapper<WxMsg> {

    /**
     * 获取分组后的消息列表
     *
     * @param page
     * @param wxMsgVO
     * @return
     */
    IPage<List<WxMsgVO>> listWxMsgMapGroup(Page page, @Param("query") WxMsgVO wxMsgVO);
}
