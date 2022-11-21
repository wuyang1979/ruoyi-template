package com.ruoyi.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 前台用户对象 xy_user
 *
 * @author ruoyi
 * @date 2022-08-16
 */
@Data
public class XyUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long userId;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;

    /**
     * 企业id
     */
    @Excel(name = "企业id")
    private Long enterpriseId;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码")
    private String phonenumber;

    /**
     * 密码
     */
    @Excel(name = "密码")
    private String password;

    /**
     * 微信头像
     */
    @Excel(name = "微信头像")
    private String headimgurl;

    /**
     * openid
     */
    @Excel(name = "openid")
    private String openid;

    /**
     * 帐号状态（0正常 1停用）
     */
    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 用户类型(0专家 1企业 )
     */
    @Excel(name = "用户类型(0专家 1企业 )")
    private String userType;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;

    /**
     * 最后登录IP
     */
    @Excel(name = "最后登录IP")
    private String loginIp;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date loginDate;
}
