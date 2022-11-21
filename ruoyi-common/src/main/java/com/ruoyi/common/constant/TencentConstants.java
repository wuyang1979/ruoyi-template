package com.ruoyi.common.constant;

/**
 * 腾讯短信配置
 *
 * @author yuxiansong
 */
public class TencentConstants {
    /**
     * 短信 appid
     */
    private String appId;
    /**
     * 腾讯云账户密钥对: secretId
     */
    private String secretId;
    /**
     * 腾讯云账户密钥对: secretKey
     */
    private String secretKey;
    /**
     * SmsSdkAppid
     */
    private String smsSdkAppId;
    /**
     * sms sign   : sms签名
     */
    private String smsSign;
    /**
     * sms 短信模板id:
     */
    private String templateId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSmsSdkAppId() {
        return smsSdkAppId;
    }

    public void setSmsSdkAppId(String smsSdkAppId) {
        this.smsSdkAppId = smsSdkAppId;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    public String getSmsSign() {
        return smsSign;
    }

    public void setSmsSign(String smsSign) {
        this.smsSign = smsSign;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}
