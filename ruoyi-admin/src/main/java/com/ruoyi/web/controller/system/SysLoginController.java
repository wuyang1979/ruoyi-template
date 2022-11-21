package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 登录验证
 *
 * @author ruoyi
 */
@RestController
@Api(tags = "登录")
public class SysLoginController {
    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISysMenuService menuService;

    @Autowired
    private SysPermissionService permissionService;

    /**
     * 登录方式
     *
     * @return 结果
     */
    @ApiOperation("登录方式")
    @GetMapping("/login/type")
    public AjaxResult loginType() {
        AjaxResult ajax = AjaxResult.success();
        String loginType = sysConfigService.selectConfigByKey("xyLoginType");
        JSONArray jsonObject = JSONArray.parseArray(loginType);
        ajax.put("data", jsonObject);
        return ajax;
    }


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public AjaxResult login(@RequestBody LoginBody loginBody) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String token = loginService.login(loginBody);
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }

    /**
     * 验证码登录
     *
     * @param loginBody
     * @return
     */
    @ApiOperation("验证码登录")
    @PostMapping("/sms/login")
    public AjaxResult smsLogin(@RequestBody LoginBody loginBody) {
        String mobile = loginBody.getMobile();
        String smsCode = loginBody.getSmsCode();
        String uuid = loginBody.getUuid();
        AjaxResult ajax = loginService.smsLogin(mobile, smsCode,
                uuid);
        return ajax;
    }

    @PostMapping("/login/validate")
    public AjaxResult loginValidate(String token) {
        AjaxResult ajax = AjaxResult.success();
        // 生成令牌
        String newToken = loginService.loginValidate(token);
        ajax.put(Constants.TOKEN, newToken);
        return ajax;
    }


    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public AjaxResult getInfo() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);
        return ajax;
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("getRouters")
    public AjaxResult getRouters() {
        Long userId = SecurityUtils.getUserId();
        List<SysMenu> menus = menuService.selectMenuTreeByUserId(userId);
        return AjaxResult.success(menuService.buildMenus(menus));
    }
}
