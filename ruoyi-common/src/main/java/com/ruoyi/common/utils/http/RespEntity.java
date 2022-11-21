package com.ruoyi.common.utils.http;


/**
 * Http请求返回
 *
 * @author liuc
 * @version 1.0
 * @date 2021-07-12 09:52:34
 */
public class RespEntity {

    private String result;
    private boolean success;

    public RespEntity() {
        super();
    }

    public RespEntity(String result, boolean success) {
        super();
        this.result = result;
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        if (result != null) {
            this.result = result;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static RespEntity success(String result) {
        return new RespEntity(result, true);
    }

    public static RespEntity failure(String result) {
        return new RespEntity(result, false);
    }

}
