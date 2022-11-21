package com.ruoyi.domain.vo;

import com.ruoyi.domain.WxOpenidInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yuanjie
 * @date 2022/10/13
 */
@Data
public class WeixinUserListVo {
    @ApiModelProperty("关注该公众账号的总用户数")
    private Integer total;

    @ApiModelProperty("拉取的OPENID个数，最大值为10000")
    private Integer count;

    @ApiModelProperty("列表数据，OPENID的列表")
    private WxOpenidInfo data;

    @ApiModelProperty("拉取列表的最后一个用户的OPENID")
    private String next_openid;

    @ApiModelProperty("错误编码")
    private int errcode;

    @ApiModelProperty("错误提示")
    private String errmsg = "ok";
}
