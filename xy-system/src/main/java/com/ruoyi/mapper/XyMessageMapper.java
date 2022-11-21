package com.ruoyi.mapper;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.domain.XyMessage;
import com.ruoyi.domain.XyMessageUser;
import com.ruoyi.domain.XyUser;
import com.ruoyi.domain.form.XyMessageForm;
import com.ruoyi.domain.vo.XyMessageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Mapper
public interface XyMessageMapper {
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
    public List<XyMessageVo> selectXyMessageList(XyMessage xyMessage);

    /**
     * 新增消息
     *
     * @param xyMessage 消息
     * @return 结果
     */
    public int insertXyMessage(XyMessageForm xyMessage);

    /**
     * 修改消息
     *
     * @param xyMessage 消息
     * @return 结果
     */
    public int updateXyMessage(XyMessageForm xyMessage);

    /**
     * 删除消息
     *
     * @param id 消息主键
     * @return 结果
     */
    public int deleteXyMessageById(Long id);

    /**
     * 批量删除消息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyMessageByIds(Long[] ids);

    int insertXyMessageUser(@Param("userList") List<XyMessageUser> userList);

    int deleteXyMessageUserById(Long id);

    List<SysUser> selectXyUserList(String keyWord);

    List<Long> getAllUserIds();

    List<Long> getExpertIds();

    List<Long> getEnterpriseIdList();

    List<Long> getSupplierIds();

    /**
     * 查询用户
     *
     * @param userId
     * @return
     */
    XyUser getXyUser(Long userId);

    /**
     * 查询消息
     *
     * @param userId
     * @return
     */
    List<XyMessageVo> getXyUserMsg(@Param("userId") Long userId, @Param("title") String title);
}
