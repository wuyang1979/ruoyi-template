package com.ruoyi.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信发布
 *
 * @author www.joolun.com
 * @date 2022-03-10 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/freepublish")
@Api(value = "freepublish", tags = "微信发布")
public class WxFreePublishController extends BaseController {
    private final WxMpService wxService;

    /**
     * 删除发布
     *
     * @param
     * @return R
     */
    @ApiOperation(value = "删除发布")
    @DeleteMapping
    @PreAuthorize("@ss.hasPermi('wxmp:wxfreepublish:del')")
    public AjaxResult del(String id) throws Exception {
        WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
        return AjaxResult.success(wxMpFreePublishService.deletePushAllArticle(id));
    }

    /**
     * 获取成功发布列表
     *
     * @param page 获取成功发布列表
     * @param
     * @return
     */
    @ApiOperation(value = "获取成功发布列表")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('wxmp:wxfreepublish:index')")
    public AjaxResult getPage(Page page) throws Exception {
        WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
        int count = (int) page.getSize();
        int offset = (int) page.getCurrent() * count - count;
        return AjaxResult.success(wxMpFreePublishService.getPublicationRecords(offset, count));
    }

}
