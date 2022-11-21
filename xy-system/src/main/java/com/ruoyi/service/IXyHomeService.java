package com.ruoyi.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.domain.vo.XyModuleCountVo;
import com.ruoyi.domain.vo.XyOrderCount;
import com.ruoyi.domain.vo.XyTotalCountVo;


/**
 * @author yuanjie
 * @date 2022/9/23
 */

public interface IXyHomeService {
    /**
     * 平台新增/总量
     */
    XyTotalCountVo getTotalCount();

    /**
     * 累计订单数量
     */
    XyOrderCount orderCount();

    /**
     * 获取累计订单金额
     *
     * @return
     */
    AjaxResult getOrderMoney();

    /**
     * 获取用户活跃度
     *
     * @return
     */
    AjaxResult getUserActivity();

    /**
     * 板块内容
     *
     * @return
     */
    XyModuleCountVo getModuleCount();
}
