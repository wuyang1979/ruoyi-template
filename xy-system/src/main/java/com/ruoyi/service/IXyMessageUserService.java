package com.ruoyi.service;

import com.ruoyi.domain.XyMessageUser;

import java.util.List;

/**
 * 消息用户关系Service接口
 *
 * @author ruoyi
 * @date 2022-08-11
 */
public interface IXyMessageUserService {
    /**
     * 查询消息用户关系
     *
     * @param id 消息用户关系主键
     * @return 消息用户关系
     */
    public XyMessageUser selectXyMessageUserById(Long id);

    /**
     * 查询消息用户关系列表
     *
     * @param xyMessageUser 消息用户关系
     * @return 消息用户关系集合
     */
    public List<XyMessageUser> selectXyMessageUserList(XyMessageUser xyMessageUser);

    /**
     * 新增消息用户关系
     *
     * @param xyMessageUser 消息用户关系
     * @return 结果
     */
    public int insertXyMessageUser(XyMessageUser xyMessageUser);

    /**
     * 修改消息用户关系
     *
     * @param xyMessageUser 消息用户关系
     * @return 结果
     */
    public int updateXyMessageUser(XyMessageUser xyMessageUser);

    /**
     * 批量删除消息用户关系
     *
     * @param ids 需要删除的消息用户关系主键集合
     * @return 结果
     */
    public int deleteXyMessageUserByIds(Long[] ids);

    /**
     * 删除消息用户关系信息
     *
     * @param id 消息用户关系主键
     * @return 结果
     */
    public int deleteXyMessageUserById(Long id);
}
