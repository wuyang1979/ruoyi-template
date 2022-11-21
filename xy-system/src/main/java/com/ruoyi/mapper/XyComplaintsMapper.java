package com.ruoyi.mapper;

import com.ruoyi.domain.XyComplaints;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 投诉建议Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Mapper
public interface XyComplaintsMapper {
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
     * 删除投诉建议
     *
     * @param id 投诉建议主键
     * @return 结果
     */
    public int deleteXyComplaintsById(Long id);

    /**
     * 批量删除投诉建议
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyComplaintsByIds(Long[] ids);
}
