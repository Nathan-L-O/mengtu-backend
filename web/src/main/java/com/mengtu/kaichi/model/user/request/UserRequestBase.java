package com.mengtu.kaichi.model.user.request;

import com.mengtu.kaichi.common.BaseRestRequest;

/**
 * 用户请求实体
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/25 13:52
 */
public class UserRequestBase extends BaseRestRequest {

    private static final long serialVersionUID = 1040225673741789535L;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 新密码
     */
    private String newPassword;

    /**
     * 用户号
     */
    private String userId;

    /**
     * 昵称
     */
    private String nickname;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String toAuditString() {
        return username + nickname;
    }
}
