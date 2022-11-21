package com.ruoyi.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.domain.XyFriendLink;
import com.ruoyi.service.IXyFriendLinkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 友情链接Controller
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Api(tags = "友情链接")
@RestController
@RequestMapping("/xySystem/link")
public class XyFriendLinkController extends BaseController {
    @Autowired
    private IXyFriendLinkService xyFriendLinkService;

    /**
     * 查询友情链接列表
     */
    @ApiOperation("查询友情链接列表")
    @PreAuthorize("@ss.hasPermi('xySystem:link:list')")
    @GetMapping("/list")
    public TableDataInfo list(XyFriendLink xyFriendLink) {
        startPage();
        List<XyFriendLink> list = xyFriendLinkService.selectXyFriendLinkList(xyFriendLink);
        return getDataTable(list);
    }

    /**
     * 导出友情链接列表
     */
    @PreAuthorize("@ss.hasPermi('xySystem:link:export')")
    @Log(title = "友情链接", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyFriendLink xyFriendLink) {
        List<XyFriendLink> list = xyFriendLinkService.selectXyFriendLinkList(xyFriendLink);
        ExcelUtil<XyFriendLink> util = new ExcelUtil<XyFriendLink>(XyFriendLink.class);
        util.exportExcel(response, list, "友情链接数据");
    }

    /**
     * 获取友情链接详细信息
     */
    @ApiOperation("获取友情链接详细信息")
    @PreAuthorize("@ss.hasPermi('xySystem:link:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyFriendLinkService.selectXyFriendLinkById(id));
    }

    /**
     * 新增友情链接
     */
    @ApiOperation("新增友情链接")
    @PreAuthorize("@ss.hasPermi('xySystem:link:add')")
    @Log(title = "友情链接", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody XyFriendLink xyFriendLink) {
        xyFriendLink.setUserId(getUserId());
        xyFriendLink.setDeptId(getDeptId());
        return toAjax(xyFriendLinkService.insertXyFriendLink(xyFriendLink));
    }

    /**
     * 修改友情链接
     */
    @ApiOperation("修改友情链接")
    @PreAuthorize("@ss.hasPermi('xySystem:link:edit')")
    @Log(title = "友情链接", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyFriendLink xyFriendLink) {
        return toAjax(xyFriendLinkService.updateXyFriendLink(xyFriendLink));
    }

    /**
     * 删除友情链接
     */
    @ApiOperation("删除友情链接")
    @PreAuthorize("@ss.hasPermi('xySystem:link:remove')")
    @Log(title = "友情链接", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyFriendLinkService.deleteXyFriendLinkByIds(ids));
    }

    /**
     * 禁用/启用友情链接
     */
    @ApiOperation("启用友情链接")
    @PreAuthorize("@ss.hasPermi('xySystem:link:status')")
    @PostMapping("/status")
    public AjaxResult status(@RequestBody XyFriendLink xyFriendLink) {
        return toAjax(xyFriendLinkService.updateXyFriendLinkStatus(xyFriendLink));
    }
}
