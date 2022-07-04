package com.mengtu.kaichi.user.user.service;

import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;

import java.util.Date;
import java.util.List;

/**
 * 用户维度基础服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:45
 */
public interface UserBasicService {

    /**
     * 登陆 带 IP
     * 构建完整的基础用户信息
     *
     * @param username
     * @param password
     * @param loginIp
     * @return
     */
    CommonUser login(String username, String password, String loginIp);

    /**
     * 通过短信验证码登陆 带 IP
     * 构建完整的基础用户信息
     *
     * @param username
     * @param loginIp
     * @return
     */
    CommonUser smsLogin(String username, String loginIp);

    /**
     * 获取用户通用模型
     *
     * @param userId
     * @return
     */
    CommonUser getByUserId(String userId);

    /**
     * 检测登陆 带 IP
     *
     * @param token
     * @param loginIp
     * @return
     */
    UserBO checkLogin(String token, String loginIp);

    /**
     * 登出
     *
     * @param userId
     * @return
     */
    void logOut(String userId);

    /**
     * 更新用户信息
     *
     * @param userId
     * @param nickname
     * @param realName
     * @param sex
     * @param mobilePhone
     * @param email
     * @param birthday
     * @param company
     * @return
     */
    void updateUserInfo(String userId, String nickname, String realName, String sex, String mobilePhone, String email, Date birthday, String company);

    /**
     * 用户鉴权
     *
     * @param userId
     * @param permIds
     * @return
     */
    boolean verifyPermissionByPermId(String userId, List<String> permIds);

    /**
     * 用户鉴权
     *
     * @param userId
     * @param permTypes
     * @return
     */
    boolean verifyPermissionByPermType(String userId, List<String> permTypes);

}
