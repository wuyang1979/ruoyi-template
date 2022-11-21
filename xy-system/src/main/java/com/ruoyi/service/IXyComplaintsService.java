package com.ruoyi.service;

import com.ruoyi.domain.XyComplaints;

import java.util.List;

/**
 * 投诉建议Service接口
 *
 * @author ruoyi
 * @date 2022-08-12
 */
public interface IXyComplaintsService {
    /**
     * 查询投诉建议
     *
     * @param id 投诉建议主键
     * @return 投诉建议
     */
    public XyComplaints selectXyComplaintsById(Long id);

    /**
     * 查询投诉建议列表
     *
     * @param xyComplaints 投诉建议
     * @return 投诉建议集合
     */
    public List<XyComplaints> selectXyComplaintsList(XyComplaints xyComplaints);

    /**
     * 新增投诉建议
     *
     * @param xyComplaints 投诉建议
     * @return 结果
     */
    public int insertXyComplaints(XyComplaints xyComplaints);

    /**
     * 修改投诉建议
     *
     * @param xyComplaints 投诉建议
     * @return 结果
     */
    public int updateXyComplaints(XyComplaints xyComplaints);

    /**
     * 批量删除投诉建议
     *
     * @param ids 需要删除的投诉建议主键集合
     * @return 结果
     */
    public int deleteXyComplaintsByIds(Long[] ids);

    /**
     * 删除投诉建议信息
     *
     * @param id 投诉建议主键
     * @return 结果
     */
    public int deleteXyComplaintsById(Long id);
}
