package com.ruoyi.service.impl;

import com.ruoyi.domain.XyMessageUser;
import com.ruoyi.mapper.XyMessageUserMapper;
import com.ruoyi.service.IXyMessageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息用户关系Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Service
public class XyMessageUserServiceImpl implements IXyMessageUserService {
    @Autowired
    private XyMessageUserMapper xyMessageUserMapper;

    /**
     * 查询消息用户关系
     *
     * @param id 消息用户关系主键
     * @return 消息用户关系
     */
    @Override
    public XyMessageUser selectXyMessageUserById(Long id) {
        return xyMessageUserMapper.selectXyMessageUserById(id);
    }

    /**
     * 查询消息用户关系列表
     *
     * @param xyMessageUser 消息用户关系
     * @return 消息用户关系
     */
    @Override
    public List<XyMessageUser> selectXyMessageUserList(XyMessageUser xyMessageUser) {
        return xyMessageUserMapper.selectXyMessageUserList(xyMessageUser);
    }

    /**
     * 新增消息用户关系
     *
     * @param xyMessageUser 消息用户关系
     * @return 结果
     */
    @Override
    public int insertXyMessageUser(XyMessageUser xyMessageUser) {
        return xyMessageUserMapper.insertXyMessageUser(xyMessageUser);
    }

    /**
     * 修改消息用户关系
     *
     * @param xyMessageUser 消息用户关系
     * @return 结果
     */
    @Override
    public int updateXyMessageUser(XyMessageUser xyMessageUser) {
        return xyMessageUserMapper.updateXyMessageUser(xyMessageUser);
    }

    /**
     * 批量删除消息用户关系
     *
     * @param ids 需要删除的消息用户关系主键
     * @return 结果
     */
    @Override
    public int deleteXyMessageUserByIds(Long[] ids) {
        return xyMessageUserMapper.deleteXyMessageUserByIds(ids);
    }

    /**
     * 删除消息用户关系信息
     *
     * @param id 消息用户关系主键
     * @return 结果
     */
    @Override
    public int deleteXyMessageUserById(Long id) {
        return xyMessageUserMapper.deleteXyMessageUserById(id);
    }
}
