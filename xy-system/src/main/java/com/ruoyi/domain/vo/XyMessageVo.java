package com.ruoyi.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.domain.XyMessage;
import lombok.Data;

/**
 * @author yuanjie
 * @date 2022/8/11
 */
@Data
public class XyMessageVo extends XyMessage {
    /**
     * 是否已读（0未读 1已读）
     */
    @Excel(name = "是否已读", readConverterExp = "0=未读,1=已读")
    private String isRead;

    /**
     * 用户名
     */
    @Excel(name = "用户名")
    private String userName;

    public XyMessageVo() {
    }

    public XyMessageVo(String isRead, String userName) {
        this.isRead = isRead;
        this.userName = userName;
    }
}
