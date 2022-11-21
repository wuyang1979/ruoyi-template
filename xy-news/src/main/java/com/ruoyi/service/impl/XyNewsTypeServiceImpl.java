package com.ruoyi.service.impl;

import java.util.List;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.domain.XyNewsType;
import com.ruoyi.mapper.XyNewsTypeMapper;
import com.ruoyi.service.IXyNewsTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 新闻类型Service业务层处理
 * 
 * @author ruoyi
 * @date 2022-08-18
 */
@Service
public class XyNewsTypeServiceImpl implements IXyNewsTypeService
{
    @Autowired
    private XyNewsTypeMapper xyNewsTypeMapper;

    /**
     * 查询新闻类型
     * 
     * @param id 新闻类型主键
     * @return 新闻类型
     */
    @Override
    public XyNewsType selectXyNewsTypeById(Long id)
    {
        return xyNewsTypeMapper.selectXyNewsTypeById(id);
    }

    /**
     * 查询新闻类型列表
     * 
     * @param xyNewsType 新闻类型
     * @return 新闻类型
     */
    @Override
    public List<XyNewsType> selectXyNewsTypeList(XyNewsType xyNewsType)
    {
        return xyNewsTypeMapper.selectXyNewsTypeList(xyNewsType);
    }

    /**
     * 新增新闻类型
     * 
     * @param xyNewsType 新闻类型
     * @return 结果
     */
    @Override
    public int insertXyNewsType(XyNewsType xyNewsType)
    {
        xyNewsType.setCreateTime(DateUtils.getNowDate());
        return xyNewsTypeMapper.insertXyNewsType(xyNewsType);
    }

    /**
     * 修改新闻类型
     * 
     * @param xyNewsType 新闻类型
     * @return 结果
     */
    @Override
    public int updateXyNewsType(XyNewsType xyNewsType)
    {
        xyNewsType.setUpdateTime(DateUtils.getNowDate());
        return xyNewsTypeMapper.updateXyNewsType(xyNewsType);
    }

    /**
     * 批量删除新闻类型
     * 
     * @param ids 需要删除的新闻类型主键
     * @return 结果
     */
    @Override
    public int deleteXyNewsTypeByIds(Long[] ids)
    {
        if (xyNewsTypeMapper.hasChildById(ids[0])>0){
            throw new ServiceException("存在子类型,不允许删除");
        }
        return xyNewsTypeMapper.deleteXyNewsTypeByIds(ids);
    }

    /**
     * 删除新闻类型信息
     * 
     * @param id 新闻类型主键
     * @return 结果
     */
    @Override
    public int deleteXyNewsTypeById(Long id)
    {
        return xyNewsTypeMapper.deleteXyNewsTypeById(id);
    }

    @Override
    public List<XyNewsType> selectTree(XyNewsType xyNewsType) {
        return xyNewsTypeMapper.selectTree(xyNewsType);
    }
}
