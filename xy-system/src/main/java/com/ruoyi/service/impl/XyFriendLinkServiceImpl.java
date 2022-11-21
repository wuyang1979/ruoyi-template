package com.ruoyi.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.domain.XyFriendLink;
import com.ruoyi.mapper.XyFriendLinkMapper;
import com.ruoyi.service.IXyFriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友情链接Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Service
public class XyFriendLinkServiceImpl implements IXyFriendLinkService {
    @Autowired
    private XyFriendLinkMapper xyFriendLinkMapper;

    /**
     * 查询友情链接
     *
     * @param id 友情链接主键
     * @return 友情链接
     */
    @Override
    public XyFriendLink selectXyFriendLinkById(Long id) {
        return xyFriendLinkMapper.selectXyFriendLinkById(id);
    }

    /**
     * 查询友情链接列表
     *
     * @param xyFriendLink 友情链接
     * @return 友情链接
     */
    @Override
    public List<XyFriendLink> selectXyFriendLinkList(XyFriendLink xyFriendLink) {
        return xyFriendLinkMapper.selectXyFriendLinkList(xyFriendLink);
    }

    /**
     * 新增友情链接
     *
     * @param xyFriendLink 友情链接
     * @return 结果
     */
    @Override
    public int insertXyFriendLink(XyFriendLink xyFriendLink) {
        xyFriendLink.setCreateTime(DateUtils.getNowDate());
        return xyFriendLinkMapper.insertXyFriendLink(xyFriendLink);
    }

    /**
     * 修改友情链接
     *
     * @param xyFriendLink 友情链接
     * @return 结果
     */
    @Override
    public int updateXyFriendLink(XyFriendLink xyFriendLink) {
        xyFriendLink.setUpdateTime(DateUtils.getNowDate());
        return xyFriendLinkMapper.updateXyFriendLink(xyFriendLink);
    }

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接主键
     * @return 结果
     */
    @Override
    public int deleteXyFriendLinkByIds(Long[] ids) {
        return xyFriendLinkMapper.deleteXyFriendLinkByIds(ids);
    }

    /**
     * 删除友情链接信息
     *
     * @param id 友情链接主键
     * @return 结果
     */
    @Override
    public int deleteXyFriendLinkById(Long id) {
        return xyFriendLinkMapper.deleteXyFriendLinkById(id);
    }

    /**
     * 禁用/启用友情链接
     *
     * @param xyFriendLink
     * @return 结果
     */
    @Override
    public int updateXyFriendLinkStatus(XyFriendLink xyFriendLink) {
        if ("0".equals(xyFriendLink.getStatus())) {
            return xyFriendLinkMapper.updateDisableStatus(xyFriendLink);
        } else {
            return xyFriendLinkMapper.updateEnableStatus(xyFriendLink);
        }
    }
}
