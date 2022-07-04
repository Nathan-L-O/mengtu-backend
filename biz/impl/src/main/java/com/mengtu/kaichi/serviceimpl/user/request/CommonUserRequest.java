package com.mengtu.kaichi.serviceimpl.user.request;

import com.mengtu.kaichi.user.request.UserManageRequest;

/**
 * 用户请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/4/24 13:32
 */
public class CommonUserRequest extends UserManageRequest {

    private static final long serialVersionUID = -4636114494303230664L;

    /**
     * 登陆凭证
     */
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
