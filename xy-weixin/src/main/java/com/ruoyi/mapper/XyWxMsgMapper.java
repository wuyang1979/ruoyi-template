package com.ruoyi.mapper;

import com.ruoyi.domain.XyUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author yuanjie
 * @date 2022/10/13
 */

public interface XyWxMsgMapper {
    /**
     * 根据openid查询用户信息
     *
     * @param id
     * @return 用户
     */
    XyUser selectByOpenid(String id);

    /**
     * 新增微信用户
     *
     * @param userInfo
     * @return
     */
    int insert(Map<String, String> userInfo);

    /**
     * 修改用户
     *
     * @param userInfo
     * @return
     */
    int update(Map<String, String> userInfo);
}
