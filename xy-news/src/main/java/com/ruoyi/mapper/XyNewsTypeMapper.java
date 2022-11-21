package com.ruoyi.mapper;

import java.util.List;

import com.ruoyi.domain.XyNewsType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 新闻类型Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-18
 */
@Mapper
public interface XyNewsTypeMapper {
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
     * 删除新闻类型
     *
     * @param id 新闻类型主键
     * @return 结果
     */
    public int deleteXyNewsTypeById(Long id);

    /**
     * 批量删除新闻类型
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyNewsTypeByIds(Long[] ids);

    /**
     * 查询类型树形结构
     *
     * @param xyNewsType 新闻类型
     * @return 结果
     */
    List<XyNewsType> selectTree(XyNewsType xyNewsType);

    /**
     * 查询子分类
     *
     * @param id
     * @return
     */
    int hasChildById(Long id);
}
