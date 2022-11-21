package com.ruoyi.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.service.XyWxMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuanjie
 * @date 2022/10/13
 */
@Api(tags = "微信消息")
@RestController
@RequestMapping("/wxMsg")
public class XyWxMsgController {
    @Autowired
    private XyWxMsgService wxMsgService;

    /**
     * 新增微信用户到xy_user
     *
     * @return 结果
     */
    @ApiOperation("新增微信用户")
    @RequestMapping("/insertWxUser")
    public AjaxResult insertWxUser() {
        return AjaxResult.success(wxMsgService.insertWxUser());
    }
}
