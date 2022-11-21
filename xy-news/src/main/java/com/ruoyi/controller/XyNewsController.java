package com.ruoyi.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.domain.XyNews;
import com.ruoyi.domain.form.NewsApprovalForm;
import com.ruoyi.domain.vo.*;
import com.ruoyi.service.IXyNewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 新闻动态Controller
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Api(tags = "新闻动态")
@RestController
@RequestMapping("/xyNews/news")
public class XyNewsController extends BaseController {
    @Autowired
    private IXyNewsService xyNewsService;

    /**
     * 查询新闻动态列表
     */
    @ApiOperation("查询新闻动态列表")
    @PreAuthorize("@ss.hasPermi('xyNews:news:list')")
    @GetMapping("/list")
    public TableDataInfo list(XyNews xyNews) {
        startPage();
        List<XyNewsVo> list = xyNewsService.selectXyNewsList(xyNews);
        return getDataTable(list);
    }

    /**
     * 查询待审核状态新闻列表
     */
    @ApiOperation("查询待审核状态新闻列表")
    @PreAuthorize("@ss.hasPermi('xyNews:news:draftList')")
    @GetMapping("/draftList")
    public TableDataInfo draftList(XyNews xyNews) {
        startPage();
        List<XyNewsVo> list = xyNewsService.selectXyNewsDraftList(xyNews);
        return getDataTable(list);
    }

    /**
     * 新闻审批
     */
    @ApiOperation("新闻审批")
    @PreAuthorize("@ss.hasPermi('xyNews:news:approval')")
    @PostMapping("/approval")
    public AjaxResult approval(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.approval(xyNews, getUsername()));
    }

    /**
     * 查询待发布/已发布新闻列表
     */
    @ApiOperation("查询待发布/已发布新闻列表")
    @PreAuthorize("@ss.hasPermi('xyNews:news:toReleasedList')")
    @GetMapping("/toReleasedList")
    public TableDataInfo toReleasedList(XyNews xyNews) {
        startPage();
        List<XyNewsVo> list = xyNewsService.toReleasedList(xyNews);
        return getDataTable(list);
    }

    /**
     * 新闻发布/新闻撤销
     */
    @ApiOperation("新闻发布/新闻撤销")
    @PostMapping("/releaseOrCancel")
    public AjaxResult releaseOrCancel(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.releaseOrCancel(xyNews, getUsername()));
    }

    /**
     * 导出新闻动态列表
     */
    @ApiOperation("导出新闻动态列表")
    @PreAuthorize("@ss.hasPermi('xyNews:news:export')")
    @Log(title = "新闻动态", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyNews xyNews) {
        List<XyNewsVo> list = xyNewsService.selectXyNewsList(xyNews);
        ExcelUtil<XyNewsVo> util = new ExcelUtil<XyNewsVo>(XyNewsVo.class);
        util.exportExcel(response, list, "新闻动态数据");
    }

    /**
     * 获取新闻动态详细信息
     */
    @ApiOperation("获取新闻动态详细信息")
    @PreAuthorize("@ss.hasPermi('xyNews:news:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyNewsService.selectXyNewsById(id));
    }

    /**
     * 新增新闻动态
     */
    @ApiOperation("新增新闻动态")
    @PreAuthorize("@ss.hasPermi('xyNews:news:add')")
    @Log(title = "新闻动态", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.insertXyNews(xyNews));
    }

    /**
     * 修改新闻动态
     */
    @ApiOperation("修改新闻动态")
    @PreAuthorize("@ss.hasPermi('xyNews:news:edit')")
    @Log(title = "新闻动态", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.updateXyNews(xyNews));
    }

    /**
     * 新闻动态置顶或推荐
     */
    @ApiOperation("新闻动态置顶或推荐")
    @PostMapping("/recommendTop")
    public AjaxResult recommendTop(@RequestBody XyNews xyNews) {
        return toAjax(xyNewsService.recommendTop(xyNews));
    }

    /**
     * 删除新闻动态
     */
    @ApiOperation("删除新闻动态")
    @PreAuthorize("@ss.hasPermi('xyNews:news:remove')")
    @Log(title = "新闻动态", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyNewsService.deleteXyNewsByIds(ids));
    }

    /**
     * 批量审批新闻
     */
    @ApiOperation("批量审批新闻")
    @PreAuthorize("@ss.hasPermi('xyNews:news:batch')")
    @PostMapping("/batchApproval")
    public AjaxResult batchApproval(@RequestBody NewsApprovalForm form) {
        form.setUserId(getUserId());
        form.setDeptId(getDeptId());
        return toAjax(xyNewsService.batchApproval(form));
    }

    /**
     * 提交审核
     */
    @ApiOperation("提交审核")
    @PreAuthorize("@ss.hasPermi('xyNews:news:submit')")
    @PostMapping("/submit")
    public AjaxResult submitApproval(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.submitApproval(xyNews));
    }

    /**
     * 新闻撤回
     */
    @ApiOperation("新闻撤回")
    @PreAuthorize("@ss.hasPermi('xyNews:news:withdraw')")
    @PostMapping("/withdraw")
    public AjaxResult withdraw(@RequestBody XyNews xyNews) {
        xyNews.setUserId(getUserId());
        xyNews.setDeptId(getDeptId());
        return toAjax(xyNewsService.withdraw(xyNews));
    }

    /**
     * 新闻推送微信公众号
     */
    @ApiOperation("新闻推送微信公众号")
    @PreAuthorize("@ss.hasPermi('xyNews:news:pushMsg')")
    @PostMapping("/pushMsg")
    public AjaxResult pushMsg(@RequestBody XyNews xyNews) {
        return xyNewsService.pushMsg(xyNews);
    }
}
