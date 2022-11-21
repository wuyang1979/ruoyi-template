package com.ruoyi.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 友情链接对象 xy_friend_link
 *
 * @author ruoyi
 * @date 2022-08-11
 */
@Data
public class XyFriendLink extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 图片地址
     */
    @Excel(name = "图片地址")
    private String cover;

    /**
     * 链接标题
     */
    @Excel(name = "链接标题")
    private String title;

    /**
     * 链接地址
     */
    @Excel(name = "链接地址")
    private String url;

    /**
     * 255
     */
    @Excel(name = "255")
    private String sort;

    /**
     * 类型（0-广告图1 -友链）
     */
    @Excel(name = "类型", readConverterExp = "0=-广告图1,-=友链")
    private String type;

    /**
     * 位置（0-中部 1-底部）
     */
    @Excel(name = "位置", readConverterExp = "0=-中部,1=-底部")
    private String position;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 状态值
     */
    @Excel(name = "状态值")
    private String statusValue;

    /**
     * 用户id
     */
    @Excel(name = "用户id")
    private Long userId;

    /**
     * 部门id
     */
    @Excel(name = "部门id")
    private Long deptId;


    public XyFriendLink() {
    }

    public XyFriendLink(Long id, String cover, String title, String url, String sort, String type, String position, String status, String statusValue) {
        this.id = id;
        this.cover = cover;
        this.title = title;
        this.url = url;
        this.sort = sort;
        this.type = type;
        this.position = position;
        this.status = status;
        this.statusValue = statusValue;
    }
}
