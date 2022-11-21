package com.ruoyi.mapper;

import com.ruoyi.domain.XyFriendLink;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 友情链接Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Mapper
public interface XyFriendLinkMapper {
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
     * 删除友情链接
     *
     * @param id 友情链接主键
     * @return 结果
     */
    public int deleteXyFriendLinkById(Long id);

    /**
     * 批量删除友情链接
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyFriendLinkByIds(Long[] ids);

    /**
     * 禁用状态
     *
     * @param xyFriendLink
     * @return
     */
    int updateDisableStatus(XyFriendLink xyFriendLink);

    /**
     * 启用状态
     *
     * @param xyFriendLink
     * @return
     */
    int updateEnableStatus(XyFriendLink xyFriendLink);
}
