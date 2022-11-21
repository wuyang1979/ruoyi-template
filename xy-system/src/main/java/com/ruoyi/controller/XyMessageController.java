package com.ruoyi.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.domain.XyMessage;
import com.ruoyi.domain.form.XyMessageForm;
import com.ruoyi.domain.vo.XyMessageVo;
import com.ruoyi.service.IXyMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 消息Controller
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Api(tags = "消息通知")
@RestController
@RequestMapping("/xySystem/message")
public class XyMessageController extends BaseController {
    @Autowired
    private IXyMessageService xyMessageService;

    /**
     * 查询消息列表
     */
    @ApiOperation("查询消息列表")
    @PreAuthorize("@ss.hasPermi('xySystem:message:list')")
    @GetMapping("/list")
    public TableDataInfo list(XyMessage xyMessage) {
        startPage();
        List<XyMessageVo> list = xyMessageService.selectXyMessageList(xyMessage, getUserId());
        return getDataTable(list);
    }

    /**
     * 下拉框查询所有用户
     */
    @ApiOperation("下拉框查询所有用户")
    @GetMapping("/userList")
    public TableDataInfo userList(SysUser user) {
        startPage();
        List<SysUser> list = xyMessageService.selectXyUserList(user.getSearchValue());
        return getDataTable(list);
    }

    /**
     * 导出消息列表
     */
    @ApiOperation("导出消息列表")
    @PreAuthorize("@ss.hasPermi('xySystem:message:export')")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, XyMessage xyMessage) {
        List<XyMessageVo> list = xyMessageService.selectXyMessageList(xyMessage, getUserId());
        ExcelUtil<XyMessageVo> util = new ExcelUtil<XyMessageVo>(XyMessageVo.class);
        util.exportExcel(response, list, "消息数据");
    }

    /**
     * 获取消息详细信息
     */
    @ApiOperation("获取消息详细信息")
    @PreAuthorize("@ss.hasPermi('xySystem:message:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(xyMessageService.selectXyMessageById(id));
    }

    /**
     * 新增消息
     */
    @ApiOperation("新增消息")
    @PreAuthorize("@ss.hasPermi('xySystem:message:add')")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult add(@RequestBody XyMessageForm form) {
        return toAjax(xyMessageService.insertXyMessage(form, getUsername()));
    }

    /**
     * 修改消息
     */
    @ApiOperation("修改消息")
    @PreAuthorize("@ss.hasPermi('xySystem:message:edit')")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PutMapping("/edit")
    public AjaxResult edit(@RequestBody XyMessageForm form) {
        return toAjax(xyMessageService.updateXyMessage(form, getUsername()));
    }

    /**
     * 删除消息
     */
    @ApiOperation("删除消息")
    @PreAuthorize("@ss.hasPermi('xySystem:message:remove')")
    @Log(title = "消息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(xyMessageService.deleteXyMessageByIds(ids));
    }
}
