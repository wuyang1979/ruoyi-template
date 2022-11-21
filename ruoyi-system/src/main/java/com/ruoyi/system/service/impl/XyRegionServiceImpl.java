package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.XyRegionMapper;
import com.ruoyi.system.domain.XyRegion;
import com.ruoyi.system.service.IXyRegionService;

/**
 * 省市区Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-24
 */
@Service
public class XyRegionServiceImpl implements IXyRegionService {
    @Autowired
    private XyRegionMapper xyRegionMapper;

    /**
     * 查询省市区
     *
     * @param regionId 省市区主键
     * @return 省市区
     */
    @Override
    public XyRegion selectXyRegionByRegionId(Integer regionId) {
        return xyRegionMapper.selectXyRegionByRegionId(regionId);
    }

    /**
     * 查询省市区列表
     *
     * @param xyRegion 省市区
     * @return 省市区
     */
    @Override
    public List<XyRegion> selectXyRegionList(XyRegion xyRegion) {
        return xyRegionMapper.selectXyRegionList(xyRegion);
    }

    /**
     * 新增省市区
     *
     * @param xyRegion 省市区
     * @return 结果
     */
    @Override
    public int insertXyRegion(XyRegion xyRegion) {
        xyRegion.setCreateTime(DateUtils.getNowDate());
        return xyRegionMapper.insertXyRegion(xyRegion);
    }

    /**
     * 修改省市区
     *
     * @param xyRegion 省市区
     * @return 结果
     */
    @Override
    public int updateXyRegion(XyRegion xyRegion) {
        xyRegion.setUpdateTime(DateUtils.getNowDate());
        return xyRegionMapper.updateXyRegion(xyRegion);
    }

    /**
     * 批量删除省市区
     *
     * @param regionIds 需要删除的省市区主键
     * @return 结果
     */
    @Override
    public int deleteXyRegionByRegionIds(Integer[] regionIds) {
        return xyRegionMapper.deleteXyRegionByRegionIds(regionIds);
    }

    /**
     * 删除省市区信息
     *
     * @param regionId 省市区主键
     * @return 结果
     */
    @Override
    public int deleteXyRegionByRegionId(Integer regionId) {
        return xyRegionMapper.deleteXyRegionByRegionId(regionId);
    }

    @Override
    public List<XyRegion> selectXyRegionLisByParentCode(String parentCode) {
        return xyRegionMapper.selectXyRegionLisByParentCode(parentCode);
    }
}
