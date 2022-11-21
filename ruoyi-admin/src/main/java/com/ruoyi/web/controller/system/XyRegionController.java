package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.XyRegion;
import com.ruoyi.system.service.IXyRegionService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 省市区Controller
 *
 * @author ruoyi
 * @date 2022-08-24
 */
@Api(tags = "省市区")
@RestController
@RequestMapping("/system/region")
public class XyRegionController extends BaseController {
    @Autowired
    private IXyRegionService xyRegionService;

    /**
     * 查询省市区列表
     */
    @ApiOperation("查询省市区列表")
    @GetMapping("/list")
    public AjaxResult list(XyRegion xyRegion) {
        List<XyRegion> list = xyRegionService.selectXyRegionList(xyRegion);
        return AjaxResult.success(list);
    }

    /**
     * 导出省市区列表
     */
    @ApiOperation("导出省市区列表")
    @Log(title = "省市区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyRegion xyRegion) {
        List<XyRegion> list = xyRegionService.selectXyRegionList(xyRegion);
        ExcelUtil<XyRegion> util = new ExcelUtil<XyRegion>(XyRegion.class);
        util.exportExcel(response, list, "省市区数据");
    }

    /**
     * 获取省市区详细信息
     */
    @ApiOperation("获取省市区详细信息")
    @GetMapping(value = "/{regionId}")
    public AjaxResult getInfo(@PathVariable("regionId") Integer regionId) {
        return AjaxResult.success(xyRegionService.selectXyRegionByRegionId(regionId));
    }

    /**
     * 新增省市区
     */
    @ApiOperation("新增省市区")
    @Log(title = "省市区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XyRegion xyRegion) {
        return toAjax(xyRegionService.insertXyRegion(xyRegion));
    }

    /**
     * 修改省市区
     */
    @ApiOperation("修改省市区")
    @Log(title = "省市区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyRegion xyRegion) {
        return toAjax(xyRegionService.updateXyRegion(xyRegion));
    }

    /**
     * 删除省市区
     */
    @ApiOperation("删除省市区")
    @Log(title = "省市区", businessType = BusinessType.DELETE)
    @DeleteMapping("/{regionIds}")
    public AjaxResult remove(@PathVariable Integer[] regionIds) {
        return toAjax(xyRegionService.deleteXyRegionByRegionIds(regionIds));
    }


    @ApiOperation("根据ParentCode查询列表")
    @GetMapping(value = "list/{parentCode}")
    public AjaxResult regionList(@PathVariable("parentCode") String parentCode) {
        List<XyRegion> list = xyRegionService.selectXyRegionLisByParentCode(parentCode);
        return AjaxResult.success(list);
    }
}
