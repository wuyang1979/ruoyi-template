package com.ruoyi.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.domain.XyNews;
import com.ruoyi.domain.form.NewsApprovalForm;
import com.ruoyi.domain.vo.XyNewsVo;

import java.util.List;

/**
 * 新闻动态Service接口
 *
 * @author ruoyi
 * @date 2022-08-12
 */
public interface IXyNewsService {
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
     * 批量删除新闻动态
     *
     * @param ids 需要删除的新闻动态主键集合
     * @return 结果
     */
    public int deleteXyNewsByIds(Long[] ids);

    /**
     * 删除新闻动态信息
     *
     * @param id 新闻动态主键
     * @return 结果
     */
    public int deleteXyNewsById(Long id);

    List<XyNewsVo> selectXyNewsDraftList(XyNews xyNews);

    int approval(XyNews xyNews, String userName);

    List<XyNewsVo> toReleasedList(XyNews xyNews);

    int releaseOrCancel(XyNews xyNews, String username);

    int batchApproval(NewsApprovalForm form);

    /**
     * 提交审核
     *
     * @param xyNews
     * @return
     */
    int submitApproval(XyNews xyNews);

    /**
     * 新闻撤回
     *
     * @param xyNews
     * @return
     */
    int withdraw(XyNews xyNews);

    /**
     * 推送微信公众号消息
     *
     * @param xyNews
     * @return
     */
    AjaxResult pushMsg(XyNews xyNews);

    /**
     * 新闻置顶或推荐
     *
     * @param xyNews
     * @return
     */
    int recommendTop(XyNews xyNews);
}
