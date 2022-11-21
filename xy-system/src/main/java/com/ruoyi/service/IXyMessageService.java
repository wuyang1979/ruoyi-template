package com.ruoyi.service;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.domain.XyMessage;
import com.ruoyi.domain.form.XyMessageForm;
import com.ruoyi.domain.vo.XyMessageVo;

import java.util.List;

/**
 * 消息Service接口
 *
 * @author ruoyi
 * @date 2022-08-11
 */
public interface IXyMessageService {
    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    public XyMessage selectXyMessageById(Long id);

    /**
     * 查询消息列表
     *
     * @param xyMessage 消息
     * @return 消息集合
     */
    public List<XyMessageVo> selectXyMessageList(XyMessage xyMessage, Long userId);

    /**
     * 新增消息
     *
     * @param form 消息
     * @return 结果
     */
    public int insertXyMessage(XyMessageForm form, String userName);

    /**
     * 修改消息
     *
     * @param xyMessage 消息
     * @return 结果
     */
    public int updateXyMessage(XyMessageForm xyMessage, String userName);

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的消息主键集合
     * @return 结果
     */
    public int deleteXyMessageByIds(Long[] ids);

    /**
     * 删除消息信息
     *
     * @param id 消息主键
     * @return 结果
     */
    public int deleteXyMessageById(Long id);

    List<SysUser> selectXyUserList(String keyWord);
}
