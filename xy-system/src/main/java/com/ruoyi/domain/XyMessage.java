package com.ruoyi.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 消息对象 xy_message
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Data
public class XyMessage extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 消息内容
     */
    @Excel(name = "消息内容")
    private String content;

    /**
     * 消息类型(0系统消息 1审核消息)
     */
    @Excel(name = "消息类型(0系统消息 1审核消息)")
    private String msgType;

    /**
     * 用户类型（0全部 1专家 2企业 3供应商 4指定用户）
     */
    @Excel(name = "用户类型（0全部 1专家 2企业 3供应商 4指定用户）")
    private String userType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    public XyMessage() {
    }

    public XyMessage(Long id, String title, String content, String msgType, String userType, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.msgType = msgType;
        this.userType = userType;
        this.status = status;
    }
}
