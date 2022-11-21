package com.ruoyi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.FieldAlias;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 新闻动态对象 xy_news
 * 
 * @author ruoyi
 * @date 2022-08-12
 */
@Data
public class XyNews extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 新闻表id */
    private Long id;

    /** 新闻类型id */
    @FieldAlias("新闻类型id")
    @Excel(name = "新闻类型id")
    private Long newsTypeId;

    /** 新闻标题 */
    @FieldAlias("新闻标题")
    @Excel(name = "新闻标题")
    private String title;

    /** 新闻内容 */
    @Excel(name = "新闻内容")
    private String content;

    /** 作者 */
    @FieldAlias("作者")
    @Excel(name = "作者")
    private String author;

    /** 来源 */
    @FieldAlias("来源")
    @Excel(name = "来源")
    private String source;

    /** 类型（1-图文、2-视频、3-音频） */
    @FieldAlias("类型")
    @Excel(name = "类型", readConverterExp = "1-图文、2-视频、3-音频")
    private String type;

    /** 新闻分类 */
    @FieldAlias("新闻分类")
    @Excel(name = "新闻分类")
    private String classify;

    /** 封面 */
    @FieldAlias("封面")
    @Excel(name = "封面")
    private String cover;

    /** 封面类型*/
    @FieldAlias("封面类型")
    @Excel(name = "封面类型")
    private String coverType;

    /** 简介 */
    @Excel(name = "简介")
    private String introduction;

    /** 驳回原因 */
    @Excel(name = "驳回原因")
    private String reason;

    /** 推荐（0-否 1-是） */
    @Excel(name = "推荐", readConverterExp = "0=-否,1=-是")
    @FieldAlias("是否推荐")
    private String recommend;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Long views;

    /** 收藏量 */
    @Excel(name = "收藏量")
    private Integer collects;

    /** 点赞量 */
    @Excel(name = "点赞量")
    private Integer favorites;

    /** 置顶（0-否 1-是） */
    @Excel(name = "置顶", readConverterExp = "0=-否,1=-是")
    @FieldAlias("是否置顶")
    private String isTop;

    /** 状态（0-草稿 1-退回 2-待审核 3-待发布 4-已发布） */
    @Excel(name = "状态", readConverterExp = "状态（0-草稿 1-退回 2-待审核 3-待发布 4-已发布）")
    private String status;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date publishTime;

    /** 审核人 */
    @Excel(name = "审核人")
    private String approveBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approveTime;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 部门id */
    @Excel(name = "部门id")
    private Long deptId;

    /** 新增状态 */
    private String addStatus;

    /** 操作内容 */
    private String operateContent;

    /** 是否是更新 */
    private String isUpdate;
}
