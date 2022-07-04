package com.mengtu.kaichi.user.model;

import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.util.common.ToString;

/**
 * 基础用户模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:43
 */
public class BasicUser extends ToString {

    private static final long serialVersionUID = 5673886233615718982L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 登陆凭证
     */
    private String token;

    /**
     * 用户信息
     */
    private UserInfoBO userInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserInfoBO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBO userInfo) {
        this.userInfo = userInfo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
