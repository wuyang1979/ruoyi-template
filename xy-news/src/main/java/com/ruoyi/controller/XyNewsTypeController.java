package com.ruoyi.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysDictData;
import com.ruoyi.domain.XyNewsType;
import com.ruoyi.service.IXyNewsTypeService;
import com.ruoyi.system.service.ISysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 新闻类型Controller
 *
 * @author ruoyi
 * @date 2022-08-18
 */
@RestController
@Api(tags = "新闻分类")
@RequestMapping("/xyNews/type")
public class XyNewsTypeController extends BaseController {
    @Autowired
    private IXyNewsTypeService xyNewsTypeService;

    @Autowired
    private ISysDictDataService dictDataService;

    /**
     * 查询新闻类型列表
     */
    @ApiOperation("查询新闻类型列表")
    @PreAuthorize("@ss.hasPermi('xyNews:type:list')")
    @GetMapping("/list")
    public AjaxResult list(XyNewsType xyNewsType) {
        List<XyNewsType> list = xyNewsTypeService.selectXyNewsTypeList(xyNewsType);
        return AjaxResult.success(list);
    }

    /**
     * 导出新闻类型列表
     */
    @PreAuthorize("@ss.hasPermi('xyNews:type:export')")
    @Log(title = "新闻类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyNewsType xyNewsType) {
        List<XyNewsType> list = xyNewsTypeService.selectXyNewsTypeList(xyNewsType);
        ExcelUtil<XyNewsType> util = new ExcelUtil<XyNewsType>(XyNewsType.class);
        util.exportExcel(response, list, "新闻类型数据");
    }

    /**
     * 获取新闻类型详细信息
     */
    @ApiOperation("获取新闻类型详细信息")
    @PreAuthorize("@ss.hasPermi('xyNews:type:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyNewsTypeService.selectXyNewsTypeById(id));
    }

    /**
     * 新增新闻类型
     */
    @ApiOperation("新增新闻类型")
    @PreAuthorize("@ss.hasPermi('xyNews:type:add')")
    @Log(title = "新闻类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XyNewsType xyNewsType) {
        return toAjax(xyNewsTypeService.insertXyNewsType(xyNewsType));
    }

    /**
     * 修改新闻类型
     */
    @ApiOperation("修改新闻类型")
    @PreAuthorize("@ss.hasPermi('xyNews:type:edit')")
    @Log(title = "新闻类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyNewsType xyNewsType) {
        return toAjax(xyNewsTypeService.updateXyNewsType(xyNewsType));
    }

    /**
     * 删除新闻类型
     */
    @ApiOperation("删除新闻类型")
    @PreAuthorize("@ss.hasPermi('xyNews:type:remove')")
    @Log(title = "新闻类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyNewsTypeService.deleteXyNewsTypeByIds(ids));
    }

    /**
     * 树形查询新闻类型列表
     */
    @ApiOperation("树形查询新闻类型列表")
    @GetMapping("/tree")
    public AjaxResult selectTree(XyNewsType xyNewsType) {
        List<XyNewsType> list = xyNewsTypeService.selectTree(xyNewsType);
        return AjaxResult.success(list);
    }

    /**
     * 新闻类型
     */
    @GetMapping("/type")
    @ApiOperation("新闻类型")
    public AjaxResult selectType() {
        SysDictData dictData = new SysDictData();
        dictData.setDictType("xy_news_type");
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return AjaxResult.success(list);
    }
}
