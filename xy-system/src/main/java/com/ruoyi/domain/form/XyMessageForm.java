package com.ruoyi.domain.form;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @author yuanjie
 * @date 2022/8/11
 */
@Data
public class XyMessageForm extends BaseEntity {
    /**
     * 消息id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 消息类型(0系统消息 1审核消息)
     */
    private String msgType;

    /**
     * 用户类型（0全部 1专家 2企业 3供应商 4指定用户）
     */
    private String userType;

    /**
     * 状态
     */
    private String status;

    /**
     * 用户ids
     */
    private List<Long> useIds;

    public XyMessageForm() {
    }

    public XyMessageForm(Long id, String title, String content, String msgType, String userType, String status, List<Long> useIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.msgType = msgType;
        this.userType = userType;
        this.status = status;
        this.useIds = useIds;
    }
}
