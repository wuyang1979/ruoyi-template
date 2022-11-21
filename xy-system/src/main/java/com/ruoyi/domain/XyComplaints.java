package com.ruoyi.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 投诉建议对象 xy_complaints
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Data
public class XyComplaints extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    /**
     * 投诉建议内容
     */
    @Excel(name = "投诉建议内容")
    private String content;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;

    /**
     * 联系人
     */
    @Excel(name = "联系人")
    private String contactPerson;

    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String contactPhone;

    /**
     * 回复状态（0-未回复，1-已回复）
     */
    @Excel(name = "回复状态", readConverterExp = "0=-未回复，1-已回复")
    private String replyState;

    /**
     * 回复内容
     */
    @Excel(name = "回复内容")
    private String reply;

    /**
     * 是否删除（0未删除 1删除）
     */
    private String delFlag;

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
}
