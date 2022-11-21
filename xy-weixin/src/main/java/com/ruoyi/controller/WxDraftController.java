package com.ruoyi.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpDraftService;
import me.chanjar.weixin.mp.api.WxMpFreePublishService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.WxMpAddDraft;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftArticles;
import me.chanjar.weixin.mp.bean.draft.WxMpUpdateDraft;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信草稿箱
 *
 * @author www.joolun.com
 * @date 2022-03-10 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wxdraft")
@Api(value = "wxdraft", tags = "微信草稿箱")
public class WxDraftController extends BaseController {
    private final WxMpService wxService;

    /**
     * 新增图文消息
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "新增草稿箱")
    @PostMapping
    @PreAuthorize("@ss.hasPermi('wxmp:wxdraft:add')")
    public AjaxResult add(@RequestBody JSONObject data) throws Exception {
        JSONArray jSONArray = data.getJSONArray("articles");
        List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);
        WxMpAddDraft wxMpAddDraft = new WxMpAddDraft();
        wxMpAddDraft.setArticles(articles);
        WxMpDraftService wxMpDraftService = wxService.getDraftService();
        String rs = wxMpDraftService.addDraft(wxMpAddDraft);
        return AjaxResult.success(rs);
    }

    /**
     * 修改微信草稿箱
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "修改微信草稿箱")
    @PutMapping
    @PreAuthorize("@ss.hasPermi('wxmp:wxdraft:edit')")
    public AjaxResult edit(@RequestBody JSONObject data) throws Exception {
        String mediaId = data.getStr("mediaId");
        JSONArray jSONArray = data.getJSONArray("articles");
        List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);
        WxMpDraftService wxMpDraftService = wxService.getDraftService();
        WxMpUpdateDraft wxMpUpdateDraft = new WxMpUpdateDraft();
        wxMpUpdateDraft.setMediaId(mediaId);
        int index = 0;
        for (WxMpDraftArticles article : articles) {
            wxMpUpdateDraft.setIndex(index);
            wxMpUpdateDraft.setArticles(article);
            wxMpDraftService.updateDraft(wxMpUpdateDraft);
            index++;
        }
        return AjaxResult.success();
    }

    /**
     * 通过id删除微信草稿箱
     *
     * @param
     * @return R
     */
    @ApiOperation(value = "通过id删除微信草稿箱")
    @DeleteMapping
    @PreAuthorize("@ss.hasPermi('wxmp:wxdraft:del')")
    public AjaxResult del(String id) throws Exception {
        WxMpDraftService wxMpDraftService = wxService.getDraftService();
        return AjaxResult.success(wxMpDraftService.delDraft(id));
    }

    /**
     * 分页查询
     *
     * @param page 分页对象
     * @param
     * @return
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    @PreAuthorize("@ss.hasPermi('wxmp:wxdraft:index')")
    public AjaxResult getPage(Page page) throws Exception {
        WxMpDraftService wxMpDraftService = wxService.getDraftService();
        int count = (int) page.getSize();
        int offset = (int) page.getCurrent() * count - count;
        return AjaxResult.success(wxMpDraftService.listDraft(offset, count));
    }

    /**
     * 发布草稿箱
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "发布草稿箱")
    @PostMapping("/publish/{id}")
    @PreAuthorize("@ss.hasPermi('wxmp:wxdraft:publish')")
    public AjaxResult publish(@PathVariable String id) throws Exception {
        WxMpFreePublishService wxMpFreePublishService = wxService.getFreePublishService();
        wxMpFreePublishService.submit(id);
        return AjaxResult.success();
    }
}
