package com.ruoyi.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.WxConstants;
import com.ruoyi.domain.WxOpenidInfo;
import com.ruoyi.domain.XyUser;
import com.ruoyi.domain.vo.WeixinUserListVo;
import com.ruoyi.mapper.XyWxMsgMapper;
import com.ruoyi.service.XyWxMsgService;
import com.ruoyi.system.service.ISysConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuanjie
 * @date 2022/10/13
 */
@Service
public class XyWxMsgServiceImpl implements XyWxMsgService {

    @Autowired
    private XyWxMsgMapper wxMsgMapper;

    @Autowired
    private ISysConfigService sysConfigService;


    /**
     * 新增微信用户到xy_user
     *
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WeixinUserListVo insertWxUser() {
        //获取最新的access_token
        String accessToken = getAccessToken();
        RestTemplate restTemplate = new RestTemplate();
        WeixinUserListVo openIdList = null;
        synchronized (this) {
            try {
                //循环获取用户openid列表--一次获取10000
                String nextOpenid = null;
                do {
                    //微信公众号获取用户列表信息接口地址
                    String requestUrl = null;
                    if (StringUtils.isBlank(nextOpenid)) {
                        requestUrl = new StringBuffer().append("https://api.weixin.qq.com/cgi-bin/user/get?access_token=").append(accessToken).toString();
                    } else {
                        requestUrl = new StringBuffer().append("https://api.weixin.qq.com/cgi-bin/user/get?access_token=")
                                .append(accessToken).append("&next_openid=").append(nextOpenid).toString();
                    }
                    openIdList = restTemplate.getForObject(requestUrl, WeixinUserListVo.class);
                    if (openIdList != null && openIdList.getErrcode() == 0) {
                        //获取用户关注列表对象
                        WxOpenidInfo data = openIdList.getData();
                        if (data != null) {
                            //获取当前循环的openid--10000条
                            List<String> openid = data.getOpenid();
                            if (openid != null && openid.size() > 0) {
                                for (int i = 0; i < openid.size(); i += 100) {
                                    int toIndex = 100;
                                    if (i + 100 > openid.size()) {
                                        // 注意下标问题
                                        toIndex = openid.size() - i;
                                    }
                                    //循环100次，每次拿到openid列表中的集合下标;例如：(0,100),(100,200)
                                    List<String> sendList = openid.subList(i, i + toIndex);
                                    for (String id : sendList) {
                                        //根据openid和access_token查询用户信息
                                        Map<String, String> userInfo = getUserInfo(accessToken, id);
                                        //根据openid查询数据库是否已存在
                                        XyUser xyUser = wxMsgMapper.selectByOpenid(id);
                                        if (null == xyUser) {
                                            //不存在则新增,每次insert---100条
                                            int num = wxMsgMapper.insert(userInfo);
                                        } else {
                                            //存在则修改
                                            int num = wxMsgMapper.update(userInfo);
                                        }
                                    }
                                }
                            }
                        }
                        //拉取列表的最后一个用户的OPENID
                        nextOpenid = openIdList.getNext_openid();
                    } else {
                        openIdList.setErrcode(40000);
                        openIdList.setErrmsg("获取关注用户列表失败");
                        return openIdList;
                    }
                } while (openIdList.getCount() > 0);
            } catch (Exception e) {
                openIdList.setErrcode(40000);
                openIdList.setErrmsg("获取用户列表失败");
                return openIdList;
            }
        }
        return openIdList;
    }

    /**
     * 获取access_token，access_token是公众号的全局唯一接口调用凭据
     *
     * @return access_token
     */
    public String getAccessToken() {
        String configure = sysConfigService.selectConfigByKey("xyWxConfigure");
        List<WxConstants> list = JSON.parseArray(configure, WxConstants.class);
        //WxConstants wxConstants = JSONObject.parseObject(configure, WxConstants.class);
        WxConstants wxConstants = list.get(0);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map = new HashMap<>();
        map.put("appId", wxConstants.getAppId());
        map.put("secret", wxConstants.getSecret());
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

    /**
     * 根据access_token和openid获取用户详细信息
     *
     * @param access_token
     * @param openid
     * @return 用户信息
     */
    public Map<String, String> getUserInfo(String access_token, String openid) {
        String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String requestUrl = url.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);

        // 获取用户信息
        DefaultHttpClient client = null;
        Map<String, String> result = new HashMap<String, String>();
        try {
            client = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(requestUrl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String response = client.execute(httpget, responseHandler);
            JSONObject OpenidJSONO = JSONObject.parseObject(response);

            String subscribe = String.valueOf(OpenidJSONO.get("subscribe"));
            String nickname = new String(String.valueOf(OpenidJSONO.get("nickname")).getBytes("ISO8859-1"), "UTF-8");
            String sex = String.valueOf(OpenidJSONO.get("sex"));
            String language = String.valueOf(OpenidJSONO.get("language"));
            String city = new String(String.valueOf(OpenidJSONO.get("city")).getBytes("ISO8859-1"), "UTF-8");
            String province = new String(String.valueOf(OpenidJSONO.get("province")).getBytes("ISO8859-1"), "UTF-8");
            String country = new String(String.valueOf(OpenidJSONO.get("country")).getBytes("ISO8859-1"), "UTF-8");
            String headimgurl = String.valueOf(OpenidJSONO.get("headimgurl"));
            String subscribeTime = String.valueOf(OpenidJSONO.get("subscribe_time"));
            String unionid = String.valueOf(OpenidJSONO.get("unionid"));
            String openId = String.valueOf(OpenidJSONO.get("openid"));
            result.put("subscribe", subscribe);
            result.put("nickname", nickname);
            result.put("sex", sex);
            result.put("language", language);
            result.put("city", city);
            result.put("province", province);
            result.put("country", country);
            result.put("headimgurl", headimgurl);
            result.put("subscribeTime", subscribeTime);
            result.put("unionid", unionid);
            result.put("openid", openId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }
}
