package com.ruoyi.system.domain;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

/**
 * 省市区对象 xy_region
 *
 * @author ruoyi
 * @date 2022-08-24
 */
public class XyRegion extends TreeEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer regionId;

    /**
     * 上级地区编码
     */
    @Excel(name = "上级地区编码")
    @ApiModelProperty("上级地区编码")
    private Integer parentCode;

    /**
     * 地区编码
     */
    @Excel(name = "地区编码")
    @ApiModelProperty("地区编码")
    private Integer regionCode;

    /**
     * 地区名称
     */
    @Excel(name = "地区名称")
    @ApiModelProperty("地区名称")
    private String regionName;

    /**
     * 地区名称简写
     */
    @Excel(name = "地区名称简写")
    @ApiModelProperty("地区名称简写")
    private String shortName;

    /**
     * 等级
     */
    @Excel(name = "等级")
    @ApiModelProperty("等级")
    private Integer level;

    /**
     * 部门状态（0正常 1停用）
     */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    public void setRegionId(Integer regionId) {
        this.regionId = regionId;
    }

    public Integer getRegionId() {
        return regionId;
    }

    public void setParentCode(Integer parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getParentCode() {
        return parentCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getLevel() {
        return level;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("regionId", getRegionId())
                .append("parentCode", getParentCode())
                .append("regionCode", getRegionCode())
                .append("regionName", getRegionName())
                .append("shortName", getShortName())
                .append("level", getLevel())
                .append("orderNum", getOrderNum())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
