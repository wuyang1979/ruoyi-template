package com.ruoyi.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.domain.XyMessage;
import com.ruoyi.domain.XyMessageUser;
import com.ruoyi.domain.XyUser;
import com.ruoyi.domain.form.XyMessageForm;
import com.ruoyi.domain.vo.XyMessageVo;
import com.ruoyi.mapper.XyMessageMapper;
import com.ruoyi.mapper.XyMessageUserMapper;
import com.ruoyi.service.IXyMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Service
public class XyMessageServiceImpl implements IXyMessageService {
    @Autowired
    private XyMessageMapper xyMessageMapper;

    @Autowired
    private XyMessageUserMapper xyMessageUserMapper;

    /**
     * 查询消息
     *
     * @param id 消息主键
     * @return 消息
     */
    @Override
    public XyMessage selectXyMessageById(Long id) {
        return xyMessageMapper.selectXyMessageById(id);
    }

    /**
     * 查询消息列表
     *
     * @param xyMessage 消息
     * @return 消息
     */
    @Override
    public List<XyMessageVo> selectXyMessageList(XyMessage xyMessage, Long userId) {
        XyUser user = xyMessageMapper.getXyUser(userId);
        if (null != user) {
            return xyMessageMapper.getXyUserMsg(user.getUserId(), xyMessage.getTitle());
        }
        return xyMessageMapper.selectXyMessageList(xyMessage);
    }

    /**
     * 新增消息
     *
     * @param xyMessage 消息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertXyMessage(XyMessageForm xyMessage, String userName) {
        xyMessage.setCreateTime(DateUtils.getNowDate());
        xyMessage.setCreateBy(userName);
        xyMessageMapper.insertXyMessage(xyMessage);
        //全部用户
        if ("0".equals(xyMessage.getUserType())) {
            List<Long> idList = xyMessageMapper.getAllUserIds();
            xyMessage.setUseIds(idList);
        }
        //专家
        if ("1".equals(xyMessage.getUserType())) {
            List<Long> expertIdList = xyMessageMapper.getExpertIds();
            xyMessage.setUseIds(expertIdList);
        }
        //企业
        if ("2".equals(xyMessage.getUserType())) {
            List<Long> enterpriseIdList = xyMessageMapper.getEnterpriseIdList();
            xyMessage.setUseIds(enterpriseIdList);
        }
        //供应商
        if ("3".equals(xyMessage.getUserType())) {
            List<Long> supplierIds = xyMessageMapper.getSupplierIds();
            xyMessage.setUseIds(supplierIds);
        }
        List<XyMessageUser> userList = new ArrayList<>();
        for (Long useId : xyMessage.getUseIds()) {
            XyMessageUser messageUser = new XyMessageUser();
            messageUser.setMsgId(xyMessage.getId());
            messageUser.setUserId(useId);
            userList.add(messageUser);
        }
        int rows = 0;
        if (userList.size() > 0) {
            rows = xyMessageMapper.insertXyMessageUser(userList);
        }
        return rows;
    }

    /**
     * 修改消息
     *
     * @param xyMessage 消息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateXyMessage(XyMessageForm xyMessage, String userName) {
        xyMessage.setUpdateTime(DateUtils.getNowDate());
        xyMessage.setUpdateBy(userName);
        xyMessageMapper.updateXyMessage(xyMessage);
        xyMessageMapper.deleteXyMessageUserById(xyMessage.getId());
        List<XyMessageUser> userList = new ArrayList<>();
        for (Long useId : xyMessage.getUseIds()) {
            XyMessageUser messageUser = new XyMessageUser();
            messageUser.setMsgId(xyMessage.getId());
            messageUser.setUserId(useId);
            userList.add(messageUser);
        }
        return xyMessageMapper.insertXyMessageUser(userList);
    }

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的消息主键
     * @return 结果
     */
    @Override
    public int deleteXyMessageByIds(Long[] ids) {
        return xyMessageMapper.deleteXyMessageByIds(ids);
    }

    /**
     * 删除消息信息
     *
     * @param id 消息主键
     * @return 结果
     */
    @Override
    public int deleteXyMessageById(Long id) {
        return xyMessageMapper.deleteXyMessageById(id);
    }

    @Override
    public List<SysUser> selectXyUserList(String keyWord) {
        return xyMessageMapper.selectXyUserList(keyWord);
    }
}
