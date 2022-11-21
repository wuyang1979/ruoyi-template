package com.ruoyi.mapper;

import com.ruoyi.domain.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yuanjie
 * @date 2022/9/23
 */
public interface XyHomeMapper {
    /**
     * 平台新增/总量
     */
    XyTotalCountVo getTotalCount();

    /**
     * 累计订单数量
     */
    XyOrderCount orderCount();

    /**
     * 查询本月所有订单
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<XyOrderMoneyVo> getOrderMoney(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取用户活跃度
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<XyUserActivity> getUserActivity(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 获取板块内容
     *
     * @return
     */
    XyModuleCountVo getModuleCount();

    /**
     * 供需数量
     *
     * @return
     */
    Integer getSupplyCount();

    /**
     * 智库数量
     *
     * @return
     */
    Integer getThinkTankCount();

}
