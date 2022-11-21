package com.ruoyi.mapper;

import com.ruoyi.domain.XyNews;
import com.ruoyi.domain.vo.XyNewsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 新闻动态Mapper接口
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Mapper
public interface XyNewsMapper {
    /**
     * 查询新闻动态
     *
     * @param id 新闻动态主键
     * @return 新闻动态
     */
    public XyNews selectXyNewsById(Long id);

    /**
     * 查询新闻动态列表
     *
     * @param xyNews 新闻动态
     * @return 新闻动态集合
     */
    public List<XyNewsVo> selectXyNewsList(XyNews xyNews);

    /**
     * 新增新闻动态
     *
     * @param xyNews 新闻动态
     * @return 结果
     */
    public int insertXyNews(XyNews xyNews);

    /**
     * 修改新闻动态
     *
     * @param xyNews 新闻动态
     * @return 结果
     */
    public int updateXyNews(XyNews xyNews);

    /**
     * 删除新闻动态
     *
     * @param id 新闻动态主键
     * @return 结果
     */
    public int deleteXyNewsById(Long id);

    /**
     * 批量删除新闻动态
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteXyNewsByIds(Long[] ids);

    /**
     * 查询待审核状态新闻列表
     *
     * @param xyNews
     * @return
     */
    List<XyNewsVo> selectXyNewsDraftList(XyNews xyNews);

    int reject(XyNews xyNews);

    int agree(XyNews xyNews);

    List<XyNewsVo> toReleasedList(XyNews xyNews);

    int release(XyNews xyNews);

    int cancel(XyNews xyNews);

    int batchAgree(@Param("ids") List<Long> ids);

    int batchReject(@Param("ids") List<Long> ids);

    void rejectApproval(XyNews xyNews);

    void agreeApproval(XyNews xyNews);

    int insertXyNewsApprove(XyNews xyNews);

    int batchInsertAgree(@Param("news") List<XyNews> news);

    int batchInsertReject(@Param("news") List<XyNews> news);

    /**
     * 新闻撤回
     *
     * @param xyNews
     */
    void withdraw(XyNews xyNews);

    /**
     * 新增记录操作
     *
     * @param xyNews
     * @return
     */
    int insertNewsApprove(XyNews xyNews);

    /**
     * 查询微信用户
     *
     * @return openid
     */
    List<String> selectWxUser();

    /**
     * 新闻推荐
     *
     * @param xyNews
     * @return
     */
    int recommend(XyNews xyNews);

    /**
     * 新闻置顶
     *
     * @param xyNews
     * @return
     */
    int top(XyNews xyNews);
}
