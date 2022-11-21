package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;

/**
 * 首页
 *
 * @author ruoyi
 */
@Api(tags = "首页配置")
@RestController
public class SysIndexController {
    /**
     * 系统基础配置
     */
    @Autowired
    private RuoYiConfig ruoyiConfig;

    @Autowired
    private ISysConfigService sysConfigService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    public String index() {
        return StringUtils.format("欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。", ruoyiConfig.getName(), ruoyiConfig.getVersion());
    }


    /**
     * 首页版权配置
     *
     * @return 结果
     */
    @ApiOperation("首页版权配置")
    @GetMapping("/web/config")
    public AjaxResult webConfig() {
        AjaxResult ajax = AjaxResult.success();
        String loginType = sysConfigService.selectConfigByKey("xyWebConfigure");
        JSONArray jsonObject = JSONArray.parseArray(loginType);
        ajax.put("data", jsonObject);
        return ajax;
    }
}
