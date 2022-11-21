package com.ruoyi.system.mapper;

import java.util.List;

import com.ruoyi.system.domain.XyRegion;

/**
 * 省市区Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-24
 */
public interface XyRegionMapper {
    /**
     * 查询省市区
     *
     * @param regionId 省市区主键
     * @return 省市区
     */
    public XyRegion selectXyRegionByRegionId(Integer regionId);

    /**
     * 查询省市区列表
     *
     * @param xyRegion 省市区
     * @return 省市区集合
     */
    public List<XyRegion> selectXyRegionList(XyRegion xyRegion);

    /**
     * 新增省市区
     *
     * @param xyRegion 省市区
     * @return 结果
     */
    public int insertXyRegion(XyRegion xyRegion);

    /**
     * 修改省市区
     *
     * @param xyRegion 省市区
     * @return 结果
     */
    public int updateXyRegion(XyRegion xyRegion);

    /**
     * 删除省市区
     *
     * @param regionId 省市区主键
     * @return 结果
     */
    public int deleteXyRegionByRegionId(Integer regionId);

    /**
     * 批量删除省市区
     *
     * @param regionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyRegionByRegionIds(Integer[] regionIds);

    List<XyRegion> selectXyRegionLisByParentCode(String parentCode);
}
