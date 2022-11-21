package com.ruoyi.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.domain.XyComplaints;
import com.ruoyi.service.IXyComplaintsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 投诉建议Controller
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Api(tags = "企业经营情况")
@RestController
@RequestMapping("/xySystem/complaints")
public class XyComplaintsController extends BaseController {
    @Autowired
    private IXyComplaintsService xyComplaintsService;

    /**
     * 查询投诉建议列表
     */
    @ApiOperation("查询投诉建议列表")
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:list')")
    @GetMapping("/list")
    public TableDataInfo list(XyComplaints xyComplaints) {
        startPage();
        List<XyComplaints> list = xyComplaintsService.selectXyComplaintsList(xyComplaints);
        return getDataTable(list);
    }

    /**
     * 导出投诉建议列表
     */
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:export')")
    @Log(title = "投诉建议", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyComplaints xyComplaints) {
        List<XyComplaints> list = xyComplaintsService.selectXyComplaintsList(xyComplaints);
        ExcelUtil<XyComplaints> util = new ExcelUtil<XyComplaints>(XyComplaints.class);
        util.exportExcel(response, list, "投诉建议数据");
    }

    /**
     * 获取投诉建议详细信息
     */
    @ApiOperation("获取投诉建议详细信息")
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyComplaintsService.selectXyComplaintsById(id));
    }

    /**
     * 新增投诉建议
     */
    @ApiOperation("新增投诉建议")
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:add')")
    @Log(title = "投诉建议", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XyComplaints xyComplaints) {
        return toAjax(xyComplaintsService.insertXyComplaints(xyComplaints));
    }

    /**
     * 修改投诉建议
     */
    @ApiOperation("修改投诉建议")
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:edit')")
    @Log(title = "投诉建议", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyComplaints xyComplaints) {
        return toAjax(xyComplaintsService.updateXyComplaints(xyComplaints));
    }

    /**
     * 删除投诉建议
     */
    @ApiOperation("删除投诉建议")
    @PreAuthorize("@ss.hasPermi('xySystem:complaints:remove')")
    @Log(title = "投诉建议", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyComplaintsService.deleteXyComplaintsByIds(ids));
    }
}
