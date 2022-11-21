package com.ruoyi.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.domain.vo.*;
import com.ruoyi.mapper.XyHomeMapper;
import com.ruoyi.service.IXyHomeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuanjie
 * @date 2022/9/23
 */
@Service
public class XyHomeServiceImpl implements IXyHomeService {
    @Autowired
    private XyHomeMapper xyHomeMapper;

    /**
     * 平台新增/总量
     */
    @Override
    public XyTotalCountVo getTotalCount() {
        return xyHomeMapper.getTotalCount();
    }

    /**
     * 累计订单数量
     */
    @Override
    public XyOrderCount orderCount() {
        return xyHomeMapper.orderCount();
    }

    /**
     * 获取累计订单金额
     *
     * @return
     */
    @Override
    public AjaxResult getOrderMoney() {
        //最近30天数据
        String startTime = DateUtils.lastThirtyDay();
        String endTime = DateUtils.today();
        List<XyOrderMoneyVo> moneyVos = xyHomeMapper.getOrderMoney(startTime, endTime);
        if (CollectionUtils.isEmpty(moneyVos)) {
            return AjaxResult.error("暂无数据！");
        }
        //分组求和
        Map<String, BigDecimal> map = moneyVos.stream().collect(Collectors.groupingBy(XyOrderMoneyVo::getOrderTime)).entrySet().
                stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue().stream().
                filter(d -> Objects.nonNull(d.getPrice())).map(XyOrderMoneyVo::getPrice).
                reduce(BigDecimal.ZERO, BigDecimal::add)));
        return AjaxResult.success(sortByKey(map));
    }

    /**
     * 获取用户活跃度
     *
     * @return
     */
    @Override
    public AjaxResult getUserActivity() {
        //最近30天数据
        String startTime = DateUtils.lastThirtyDay();
        String endTime = DateUtils.today();
        List<XyUserActivity> userActivities = xyHomeMapper.getUserActivity(startTime, endTime);
        if (CollectionUtils.isEmpty(userActivities)) {
            return AjaxResult.error("暂无数据！");
        }
        //去重
        List<XyUserActivity> distinctClass = userActivities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(()
                -> new TreeSet<>(Comparator.comparing(o -> o.getUserName() + ";" + o.getLoginTime()))), ArrayList::new));
        Map<String, List<XyUserActivity>> map = distinctClass.stream().collect(Collectors.groupingBy(o -> o.getLoginTime()));
        Map result = new HashMap();
        for (String s : map.keySet()) {
            result.put(s, map.get(s).size());
        }
        return AjaxResult.success(sortByKey(result));
    }


    /**
     * 板块内容
     *
     * @return
     */
    @Override
    public XyModuleCountVo getModuleCount() {
        XyModuleCountVo result = xyHomeMapper.getModuleCount();
        result.setSupplyCount(xyHomeMapper.getSupplyCount());
        result.setThinkTankCount(xyHomeMapper.getThinkTankCount());
        return result;
    }

    public <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()
                ).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
}
