package com.ruoyi.domain.vo;

import lombok.Data;

/**
 * @author yuanjie
 * @date 2022/9/26
 */
@Data
public class XyOrderCount {

    /**
     * handle品订单
     */
    private Integer handleCount;

    /**
     * 需求订单
     */
    private Integer demandCount;

    public XyOrderCount() {
    }

    public XyOrderCount(Integer handleCount, Integer demandCount) {
        this.handleCount = handleCount;
        this.demandCount = demandCount;
    }
}
