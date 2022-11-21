package com.ruoyi.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.domain.XyMessageUser;
import com.ruoyi.service.IXyMessageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息用户关系Controller
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@RestController
@RequestMapping("/xySystem/MsgUser")
public class XyMessageUserController extends BaseController {
    @Autowired
    private IXyMessageUserService xyMessageUserService;

    /**
     * 查询消息用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(XyMessageUser xyMessageUser) {
        startPage();
        List<XyMessageUser> list = xyMessageUserService.selectXyMessageUserList(xyMessageUser);
        return getDataTable(list);
    }

    /**
     * 导出消息用户关系列表
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:export')")
    @Log(title = "消息用户关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyMessageUser xyMessageUser) {
        List<XyMessageUser> list = xyMessageUserService.selectXyMessageUserList(xyMessageUser);
        ExcelUtil<XyMessageUser> util = new ExcelUtil<XyMessageUser>(XyMessageUser.class);
        util.exportExcel(response, list, "消息用户关系数据");
    }

    /**
     * 获取消息用户关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyMessageUserService.selectXyMessageUserById(id));
    }

    /**
     * 新增消息用户关系
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:add')")
    @Log(title = "消息用户关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody XyMessageUser xyMessageUser) {
        return toAjax(xyMessageUserService.insertXyMessageUser(xyMessageUser));
    }

    /**
     * 修改消息用户关系
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:edit')")
    @Log(title = "消息用户关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody XyMessageUser xyMessageUser) {
        return toAjax(xyMessageUserService.updateXyMessageUser(xyMessageUser));
    }

    /**
     * 删除消息用户关系
     */
    @PreAuthorize("@ss.hasPermi('xySystem:MsgUser:remove')")
    @Log(title = "消息用户关系", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyMessageUserService.deleteXyMessageUserByIds(ids));
    }
}
