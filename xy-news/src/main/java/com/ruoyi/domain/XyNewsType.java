package com.ruoyi.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 新闻类型对象 xy_news_type
 *
 * @author ruoyi
 * @date 2022-08-18
 */
@Data
public class XyNewsType extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 新闻类型id
     */
    private Long id;

    /**
     * 类型名称
     */
    @Excel(name = "类型名称")
    private String typeName;

    /**
     * 图标名称
     */
    @Excel(name = "图标名称")
    private String icon;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;
}
