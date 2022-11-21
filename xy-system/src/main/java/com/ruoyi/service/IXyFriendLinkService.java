package com.ruoyi.service;

import com.ruoyi.domain.XyFriendLink;

import java.util.List;

/**
 * 友情链接Service接口
 *
 * @author ruoyi
 * @date 2022-08-11
 */
public interface IXyFriendLinkService {
    /**
     * 查询友情链接
     *
     * @param id 友情链接主键
     * @return 友情链接
     */
    public XyFriendLink selectXyFriendLinkById(Long id);

    /**
     * 查询友情链接列表
     *
     * @param xyFriendLink 友情链接
     * @return 友情链接集合
     */
    public List<XyFriendLink> selectXyFriendLinkList(XyFriendLink xyFriendLink);

    /**
     * 新增友情链接
     *
     * @param xyFriendLink 友情链接
     * @return 结果
     */
    public int insertXyFriendLink(XyFriendLink xyFriendLink);

    /**
     * 修改友情链接
     *
     * @param xyFriendLink 友情链接
     * @return 结果
     */
    public int updateXyFriendLink(XyFriendLink xyFriendLink);

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的友情链接主键集合
     * @return 结果
     */
    public int deleteXyFriendLinkByIds(Long[] ids);

    /**
     * 删除友情链接信息
     *
     * @param id 友情链接主键
     * @return 结果
     */
    public int deleteXyFriendLinkById(Long id);

    /**
     * 禁用/启用友情链接
     *
     * @param xyFriendLink
     * @return 结果
     */
    int updateXyFriendLinkStatus(XyFriendLink xyFriendLink);
}
