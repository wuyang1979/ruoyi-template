package com.ruoyi.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.domain.XyNews;
import lombok.Data;

/**
 * @author yuanjie
 * @date 2022/8/14
 */
@Data
public class XyNewsVo extends XyNews {
    /**
     * 新闻类型名称
     */
    @Excel(name = "新闻类型名称")
    private String typeName;

    /**
     * 状态值
     */
    @Excel(name = "状态值")
    private String statusValue;
}
