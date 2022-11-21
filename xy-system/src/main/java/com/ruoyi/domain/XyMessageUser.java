package com.ruoyi.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 消息用户关系对象 xy_message_user
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Data
public class XyMessageUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 消息id
     */
    @Excel(name = "消息id")
    private Long msgId;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;

    /**
     * 是否已读（0未读 1已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;
}
