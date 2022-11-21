package com.ruoyi.domain.form;

import lombok.Data;

import java.util.List;

/**
 * @author yuanjie
 * @date 2022/8/18
 */
@Data
public class NewsApprovalForm {
    /**
     * status
     */
    private String status;

    /**
     * 新闻表id
     */
    private List<Long> ids;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 部门id
     */
    private Long deptId;
}
