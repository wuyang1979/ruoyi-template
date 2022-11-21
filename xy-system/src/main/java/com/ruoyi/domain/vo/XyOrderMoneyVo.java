package com.ruoyi.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yuanjie
 * @date 2022/10/27
 */
@Data
public class XyOrderMoneyVo {
    /**
     * 订单时间
     */
    private String orderTime;

    /**
     * 订单价格
     */
    private BigDecimal price;

    public XyOrderMoneyVo() {
    }
}
