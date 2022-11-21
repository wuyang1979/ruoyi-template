package com.ruoyi.web.controller.system;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.TencentConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.RSAUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SmsUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserService;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.tencentcloudapi.sms.v20190711.models.SendStatus;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(tags = "生成验证码接口")
@Controller
public class GenerateSms {
    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService sysConfigService;

    @Autowired
    private ISysUserService sysUserService;

    @Value("${RSA.privateKey}")
    private String privateKey;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ApiOperation("生成验证码")
    @ApiImplicitParam(name = "mobile", value = "手机号码", required = true, dataType = "String", paramType = "query")
    @PostMapping("/sms/code")
    @ResponseBody
    public AjaxResult sms(@RequestBody LoginBody loginBody) {
        return sendSms(loginBody.getMobile());
    }

    @ApiOperation("校验用户名密码，并发送验证码")
    @PostMapping("/sms/check/code")
    @ResponseBody
    public AjaxResult checkAndSendCode(@RequestBody LoginBody loginBody) throws Exception {
        //验证用户名密码的正确性
        SysUser user = sysUserService.selectUserByUserName(loginBody.getUsername());

        if (StringUtils.isNull(user)) {

            return AjaxResult.error("登录用户：" + loginBody.getUsername() + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {

            return AjaxResult.error("对不起，您的账号：" + loginBody.getUsername() + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {

            return AjaxResult.error("对不起，您的账号：" + loginBody.getUsername() + " 已停用");
        }
        String password = new String(RSAUtils.privateDecrypt(loginBody.getPassword(), privateKey));

        if (!SecurityUtils.matchesPassword(password, user.getPassword())) {
            return AjaxResult.error("用户名密码错误");
        }
        String mobile = user.getPhonenumber();
        if (StringUtils.isBlank(mobile)) {
            return AjaxResult.error("对不起，您的账号：" + loginBody.getUsername() + " 手机号不存在");
        }
        return sendSms(mobile);
    }

    public AjaxResult sendSms(String mobile) {
        // 保存验证码信息
        String uuid = IdUtils.simpleUUID();
        String verifyKey = Constants.SMS_CAPTCHA_CODE_KEY + uuid;
        int code = (int) Math.ceil(Math.random() * 9000 + 1000);
        Map<String, Object> map = new HashMap<>(16);
        map.put("mobile", mobile);
        map.put("code", code);
        redisCache.setCacheObject(verifyKey, map, Constants.SMS_EXPIRATION, TimeUnit.MINUTES);
        logger.info(" 为 {} 设置短信验证码：{}", mobile, code);
        try {
            String configure = sysConfigService.selectConfigByKey("xyMmsConfigure");
            //TencentConstants tencentConstants = JSONObject.parseObject(configure, TencentConstants.class);
            List<TencentConstants> list = JSON.parseArray(configure, TencentConstants.class);
            TencentConstants tencentConstants = list.get(0);
            String[] valCode = new String[]{code + "", "5"};
            mobile = "86" + mobile;
            String[] mobiles = new String[]{mobile};
            SendSmsResponse response = SmsUtils.sendSms(valCode, mobiles, tencentConstants);
            //请求成功
            AjaxResult ajax = AjaxResult.success();
            ajax.put("uuid", uuid);
            if (response != null && response.getSendStatusSet().length > 0) {
                SendStatus sendStatus = response.getSendStatusSet()[0];
                if (sendStatus.getCode().equals("Ok")) {
                    return ajax;
                }
                return AjaxResult.error(response.getSendStatusSet()[0].getMessage());
            }
            return AjaxResult.error();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.error();
    }
}