package com.ruoyi.service;

import com.ruoyi.domain.XyNewsType;

import java.util.List;

/**
 * 新闻类型Service接口
 *
 * @author ruoyi
 * @date 2022-08-18
 */
public interface IXyNewsTypeService {
    /**
     * 查询新闻类型
     *
     * @param id 新闻类型主键
     * @return 新闻类型
     */
    public XyNewsType selectXyNewsTypeById(Long id);

    /**
     * 查询新闻类型列表
     *
     * @param xyNewsType 新闻类型
     * @return 新闻类型集合
     */
    public List<XyNewsType> selectXyNewsTypeList(XyNewsType xyNewsType);

    /**
     * 新增新闻类型
     *
     * @param xyNewsType 新闻类型
     * @return 结果
     */
    public int insertXyNewsType(XyNewsType xyNewsType);

    /**
     * 修改新闻类型
     *
     * @param xyNewsType 新闻类型
     * @return 结果
     */
    public int updateXyNewsType(XyNewsType xyNewsType);

    /**
     * 批量删除新闻类型
     *
     * @param ids 需要删除的新闻类型主键集合
     * @return 结果
     */
    public int deleteXyNewsTypeByIds(Long[] ids);

    /**
     * 删除新闻类型信息
     *
     * @param id 新闻类型主键
     * @return 结果
     */
    public int deleteXyNewsTypeById(Long id);

    List<XyNewsType> selectTree(XyNewsType xyNewsType);
}
