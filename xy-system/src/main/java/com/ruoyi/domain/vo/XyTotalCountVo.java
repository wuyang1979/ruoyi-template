package com.ruoyi.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yuanjie
 * @date 2022/9/23
 */
@Data
public class XyTotalCountVo implements Serializable {
    /**
     * 应用数量
     */
    private Integer applicationCount;

    /**
     * handle品数量
     */
    private Integer handleCount;

    /**
     * 企业数量
     */
    private Integer enterpriseCount;

    /**
     * 政策数量
     */
    private Integer policyCount;

    /**
     * 专家数量
     */
    private Integer expertCount;

    /**
     * 技术数量
     */
    private Integer technologyCount;

    public XyTotalCountVo() {
    }

    public XyTotalCountVo(Integer applicationCount, Integer handleCount, Integer enterpriseCount, Integer policyCount, Integer expertCount, Integer technologyCount) {
        this.applicationCount = applicationCount;
        this.handleCount = handleCount;
        this.enterpriseCount = enterpriseCount;
        this.policyCount = policyCount;
        this.expertCount = expertCount;
        this.technologyCount = technologyCount;
    }
}
