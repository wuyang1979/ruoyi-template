package com.ruoyi.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.domain.XyComplaints;
import com.ruoyi.mapper.XyComplaintsMapper;
import com.ruoyi.service.IXyComplaintsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投诉建议Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Service
public class XyComplaintsServiceImpl implements IXyComplaintsService {
    @Autowired
    private XyComplaintsMapper xyComplaintsMapper;

    /**
     * 查询投诉建议
     *
     * @param id 投诉建议主键
     * @return 投诉建议
     */
    @Override
    public XyComplaints selectXyComplaintsById(Long id) {
        return xyComplaintsMapper.selectXyComplaintsById(id);
    }

    /**
     * 查询投诉建议列表
     *
     * @param xyComplaints 投诉建议
     * @return 投诉建议
     */
    @Override
    public List<XyComplaints> selectXyComplaintsList(XyComplaints xyComplaints) {
        return xyComplaintsMapper.selectXyComplaintsList(xyComplaints);
    }

    /**
     * 新增投诉建议
     *
     * @param xyComplaints 投诉建议
     * @return 结果
     */
    @Override
    public int insertXyComplaints(XyComplaints xyComplaints) {
        xyComplaints.setCreateTime(DateUtils.getNowDate());
        return xyComplaintsMapper.insertXyComplaints(xyComplaints);
    }

    /**
     * 修改投诉建议
     *
     * @param xyComplaints 投诉建议
     * @return 结果
     */
    @Override
    public int updateXyComplaints(XyComplaints xyComplaints) {
        xyComplaints.setUpdateTime(DateUtils.getNowDate());
        xyComplaints.setReplyState("1");
        return xyComplaintsMapper.updateXyComplaints(xyComplaints);
    }

    /**
     * 批量删除投诉建议
     *
     * @param ids 需要删除的投诉建议主键
     * @return 结果
     */
    @Override
    public int deleteXyComplaintsByIds(Long[] ids) {
        return xyComplaintsMapper.deleteXyComplaintsByIds(ids);
    }

    /**
     * 删除投诉建议信息
     *
     * @param id 投诉建议主键
     * @return 结果
     */
    @Override
    public int deleteXyComplaintsById(Long id) {
        return xyComplaintsMapper.deleteXyComplaintsById(id);
    }
}
