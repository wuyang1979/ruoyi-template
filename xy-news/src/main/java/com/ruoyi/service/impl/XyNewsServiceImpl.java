package com.ruoyi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.WxConstants;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.beanCompare.BeanCompareUtils;
import com.ruoyi.common.utils.beanCompare.BeanDiff;
import com.ruoyi.common.utils.beanCompare.FieldDiff;
import com.ruoyi.common.utils.http.HttpUtil;
import com.ruoyi.common.utils.http.RespEntity;
import com.ruoyi.domain.WechatTemplate;
import com.ruoyi.domain.XyNews;
import com.ruoyi.domain.form.NewsApprovalForm;
import com.ruoyi.domain.vo.TemplateData;
import com.ruoyi.domain.vo.XyNewsVo;
import com.ruoyi.mapper.XyNewsMapper;
import com.ruoyi.service.IXyNewsService;
import com.ruoyi.system.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 新闻动态Service业务层处理
 *
 * @author ruoyi
 * @date 2022-08-12
 */
@Service
public class XyNewsServiceImpl implements IXyNewsService {
    @Resource
    private XyNewsMapper xyNewsMapper;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysConfigService sysConfigService;

    @Value("${WeXin.templateId}")
    private String templateId;

    /**
     * 查询新闻动态
     *
     * @param id 新闻动态主键
     * @return 新闻动态
     */
    @Override
    public XyNews selectXyNewsById(Long id) {
        return xyNewsMapper.selectXyNewsById(id);
    }

    /**
     * 查询新闻动态列表
     *
     * @param xyNews 新闻动态
     * @return 新闻动态
     */
    @DataScope(deptAlias = "xn", userAlias = "xn")
    @Override
    public List<XyNewsVo> selectXyNewsList(XyNews xyNews) {
        return xyNewsMapper.selectXyNewsList(xyNews);
    }

    /**
     * 新增新闻动态
     *
     * @param xyNews 新闻动态
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insertXyNews(XyNews xyNews) {
        xyNews.setCreateTime(DateUtils.getNowDate());
        xyNews.setFavorites((int) (Math.random() * 40 + 10));
        xyNews.setCollects((int) (Math.random() * 40 + 10));
        xyNews.setViews(Long.valueOf((int) (Math.random() * 50 + 50)));
        if ("0".equals(xyNews.getAddStatus())) {
            //草稿状态
            xyNews.setStatus("0");
            xyNews.setOperateContent("【保存草稿】操作，新增一条："+xyNews.getTitle()+ "的记录；并保存草稿!");
        }
        //待审核状态
        if ("2".equals(xyNews.getAddStatus())) {
            xyNews.setStatus("2");
            xyNews.setOperateContent("【提交审核】操作，新增一条："+xyNews.getTitle()+ "的记录；并提交审核!");
        }
        xyNewsMapper.insertXyNews(xyNews);
        return xyNewsMapper.insertNewsApprove(xyNews);
    }

    /**
     * 修改新闻动态
     *
     * @param xyNews 新闻动态
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateXyNews(XyNews xyNews) {
        //查询修改前信息,记录修改前后具体内容
        XyNews news = xyNewsMapper.selectXyNewsById(xyNews.getId());
        if (null == news){
            return 0;
        }
        //需要记录修改的字段
        String[] fieldArray = new String[]{"newsTypeId","title","author","source","type","classify","cover","coverType"
                ,"recommend","isTop","remark"};
        BeanDiff beanDiff = BeanCompareUtils.compareInclude(news, xyNews, fieldArray);
        List<FieldDiff> list = beanDiff.getFieldDiffList();
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i< list.size(); i ++) {
                FieldDiff fieldDiff = list.get(i);
                String s = fieldDiff.toString();
                sb.append(s).append(";");
            }
        }
        String operateContent = sb.toString();
        //草稿状态
        xyNews.setStatus("0");
        xyNews.setCreateTime(DateUtils.getNowDate());
        xyNews.setOperateContent("【保存草稿】操作，"+operateContent+"并保存草稿");
        xyNews.setUpdateTime(DateUtils.getNowDate());
        //提交审核
        if ("0".equals(xyNews.getAddStatus())) {
            xyNews.setStatus("2");
            xyNews.setOperateContent("【提交审核】操作，"+operateContent+"并提交审核");
        }
        xyNewsMapper.insertNewsApprove(xyNews);
        return xyNewsMapper.updateXyNews(xyNews);
    }

    /**
     * 批量删除新闻动态
     *
     * @param ids 需要删除的新闻动态主键
     * @return 结果
     */
    @Override
    public int deleteXyNewsByIds(Long[] ids) {
        return xyNewsMapper.deleteXyNewsByIds(ids);
    }

    /**
     * 删除新闻动态信息
     *
     * @param id 新闻动态主键
     * @return 结果
     */
    @Override
    public int deleteXyNewsById(Long id) {
        return xyNewsMapper.deleteXyNewsById(id);
    }

    @Override
    public List<XyNewsVo> selectXyNewsDraftList(XyNews xyNews) {
        return xyNewsMapper.selectXyNewsDraftList(xyNews);
    }

    /**
     * 新闻审批
     *
     * @param xyNews
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int approval(XyNews xyNews, String userName) {
        xyNews.setApproveTime(DateUtils.getNowDate());
        //1驳回申请
        if ("1".equals(xyNews.getStatus())) {
            xyNews.setOperateContent("【审核不通过】操作，说明:"+(StringUtils.isBlank(xyNews.getRemark())?"无":xyNews.getRemark()));
            xyNewsMapper.rejectApproval(xyNews);
            return xyNewsMapper.reject(xyNews);
        }
        //2审核通过
        if ("2".equals(xyNews.getStatus())) {
            xyNews.setOperateContent("【审核通过】操作");
            xyNewsMapper.agreeApproval(xyNews);
            return xyNewsMapper.agree(xyNews);
        }
        return 0;
    }

    @Override
    public List<XyNewsVo> toReleasedList(XyNews xyNews) {
        return xyNewsMapper.toReleasedList(xyNews);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int releaseOrCancel(XyNews xyNews, String username) {
        xyNews.setCreateTime(DateUtils.getNowDate());
        //2新闻发布
        if ("2".equals(xyNews.getStatus())) {
            xyNews.setPublishTime(DateUtils.getNowDate());
            xyNewsMapper.release(xyNews);
            xyNews.setStatus("4");
            xyNews.setOperateContent("【发布】操作");
        }
        //3新闻取消发布
        if ("3".equals(xyNews.getStatus())) {
            xyNewsMapper.cancel(xyNews);
            xyNews.setStatus("3");
            xyNews.setOperateContent("【取消发布】操作，说明："+(StringUtils.isBlank(xyNews.getRemark())?"无":xyNews.getRemark()));
        }
        return xyNewsMapper.insertXyNewsApprove(xyNews);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int batchApproval(NewsApprovalForm form) {
        List<XyNews> news = new ArrayList<>();
        for (Long id : form.getIds()) {
            XyNews xyNews = new XyNews();
            xyNews.setId(id);
            xyNews.setDeptId(form.getDeptId());
            xyNews.setUserId(form.getUserId());
            news.add(xyNews);
        }
        //批量通过
        if ("0".equals(form.getStatus())) {
            xyNewsMapper.batchAgree(form.getIds());
            return xyNewsMapper.batchInsertAgree(news);
        }
        //批量驳回
        if ("1".equals(form.getStatus())) {
            xyNewsMapper.batchReject(form.getIds());
            return xyNewsMapper.batchInsertReject(news);
        }
        return 0;
    }

    /**
     * 提交审核
     *
     * @param xyNews
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitApproval(XyNews xyNews) {
        xyNews.setCreateTime(DateUtils.getNowDate());
        xyNews.setStatus("2");
        xyNews.setOperateContent("【提交审核】操作");
        xyNewsMapper.insertNewsApprove(xyNews);
        return xyNewsMapper.updateXyNews(xyNews);
    }

    /**
     * 新闻撤回
     *
     * @param xyNews
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int withdraw(XyNews xyNews) {
        xyNewsMapper.withdraw(xyNews);
        xyNews.setStatus("5");
        xyNews.setCreateTime(DateUtils.getNowDate());
        xyNews.setOperateContent("【撤回】操作，说明："+(StringUtils.isBlank(xyNews.getRemark())?"无":xyNews.getRemark()));
        return xyNewsMapper.insertXyNewsApprove(xyNews);
    }

    /**
     * 推送微信公众号消息
     *
     * @param xyNews
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult pushMsg(XyNews xyNews) {
        //先去缓存找token
        String accessToken = redisCache.getCacheObject(Constants.ACCESS_TOKEN);
        if (accessToken == null){
            accessToken = getAccessToken();
            //调用获取token，塞进redis,时效2小时
            redisCache.setCacheObject(Constants.ACCESS_TOKEN, accessToken, 120, TimeUnit.MINUTES);
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        //查询微信用户openId
        List<String> openIds = xyNewsMapper.selectWxUser();
        if (CollectionUtils.isEmpty(openIds)){
           return AjaxResult.error("不存在关注用户");
        }
        for (String openId : openIds) {
            WechatTemplate wechatTemplate = new WechatTemplate();
            wechatTemplate.setTemplate_id(templateId);
            wechatTemplate.setTouser(openId);
            //跳转链接
            wechatTemplate.setUrl("https://www.baidu.com/");
            Map<String, TemplateData> param = new HashMap<>();
            TemplateData templateData1 = new TemplateData();
            templateData1.setValue("尊敬的用户您好！");
            param.put("first", templateData1);
            TemplateData templateData2 = new TemplateData();
            templateData2.setValue("测试");
            param.put("keyword1", templateData2);
            TemplateData templateData3 = new TemplateData();
            templateData3.setValue("测试");
            param.put("keyword2", templateData3);
            TemplateData templateData4 = new TemplateData();
            templateData4.setValue("测试");
            param.put("remark", templateData4);
            wechatTemplate.setData(param);
            try {
                //http调用
                RespEntity result = HttpUtil.request("POST", url, null, null, JSONObject.toJSONString(wechatTemplate));
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return AjaxResult.success();
    }

    /**
     * 新闻置顶或推荐
     *
     * @param xyNews
     * @return
     */
    @Override
    public int recommendTop(XyNews xyNews) {
        xyNews.setUpdateTime(DateUtils.getNowDate());
        //0推荐
        if ("0".equals(xyNews.getStatus())){
            return xyNewsMapper.recommend(xyNews);
        }
        //1置顶
        if ("1".equals(xyNews.getStatus())){
            return xyNewsMapper.top(xyNews);
        }
        return 0;
    }

    /**
     * 获取access_token，access_token是公众号的全局唯一接口调用凭据
     *
     * @return access_token
     */
    public String getAccessToken() {
        String configure = sysConfigService.selectConfigByKey("xyWxConfigure");
        //WxConstants wxConstants = JSONObject.parseObject(configure, WxConstants.class);
        List<WxConstants> list = JSON.parseArray(configure,WxConstants.class);
        WxConstants wxConstants = list.get(0);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("appId", wxConstants.getAppId());
        map.put("secret",wxConstants.getSecret());
        //调用微信公众号接口
        ResponseEntity<String> forEntity = restTemplate.getForEntity("https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential&appid={appId}&secret={secret}", String.class, map);
        String body = forEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        String access_token = jsonObject.getString("access_token");
        String expires_in = jsonObject.getString("expires_in");//时效--7200s
        /*Constast constast = new Constast();
        constast.setAccess_token(access_token);*/
        return access_token;
    }
}
