package com.ruoyi.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.service.IXyHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 首页Controller
 *
 * @author yuanjie
 * @date 2022/9/23
 */
@Api(tags = "首页")
@RestController
@RequestMapping("/home")
public class XyHomeController {
    @Autowired
    private IXyHomeService xyHomeService;

    /**
     * 平台新增/总量
     */
    @ApiOperation("平台新增/总量")
    @GetMapping(value = "/totalCount")
    public AjaxResult getTotalCount() {
        return AjaxResult.success(xyHomeService.getTotalCount());
    }

    /**
     * 累计订单数量
     */
    @ApiOperation("累计订单数量")
    @GetMapping(value = "/orderCount")
    public AjaxResult getOrderCount() {
        return AjaxResult.success(xyHomeService.orderCount());
    }

    /**
     * 获取累计订单金额
     */
    @ApiOperation("获取累计订单金额")
    @GetMapping(value = "/orderMoney")
    public AjaxResult getOrderMoney() {
        return xyHomeService.getOrderMoney();
    }

    /**
     * 获取用户活跃度
     */
    @ApiOperation("获取用户活跃度")
    @GetMapping(value = "/userActivity")
    public AjaxResult getUserActivity() {
        return xyHomeService.getUserActivity();
    }

    /**
     * 板块内容
     */
    @ApiOperation("板块内容")
    @GetMapping(value = "/moduleCount")
    public AjaxResult getModuleCount() {
        return AjaxResult.success(xyHomeService.getModuleCount());
    }
}
