package com.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuanjie
 * @date 2022/10/27
 */
@Data
public class XyModuleCountVo implements Serializable {
    /**
     * 行业智库数量
     */
    private Integer thinkTankCount;

    /**
     * 精准招商数量
     */
    private Integer parkCount;

    /**
     * 应用数量
     */
    private Integer appCount;

    /**
     * 新闻数量
     */
    private Integer newsCount;

    /**
     * 供需数量
     */
    private Integer supplyCount;

    public XyModuleCountVo() {
    }
}
