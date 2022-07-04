package com.mengtu.kaichi.serviceimpl.user.builder;

import com.mengtu.kaichi.serviceimpl.user.request.CommonUserRequest;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用用户请求构建器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:32
 */
final public class CommonUserRequestBuilder {

    /**
     * 角色 ID
     */
    List<String> roleIds = new ArrayList<>();

    /**
     * 权限 ID
     */
    List<String> permIds = new ArrayList<>();

    /**
     * 请求 ID
     */
    @NotBlank
    private String requestId;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 用户名
     */
    private String username;

    /**
     * 明文密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 用户信息
     */
    private UserInfoBO userInfoBO;

    /**
     * 登陆凭证
     */
    private String token;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    private CommonUserRequestBuilder() {
    }

    public static CommonUserRequestBuilder getInstance() {
        return new CommonUserRequestBuilder();
    }

    public CommonUserRequest build() {
        CommonUserRequest request = new CommonUserRequest();
        request.setRequestId(requestId);
        request.setUserId(userId);
        request.setPassword(password);
        request.setUsername(username);
        request.setSalt(salt);
        request.setUserInfoBO(userInfoBO);
        request.setRoleIds(roleIds);
        request.setPermIds(permIds);
        request.setExtInfo(extInfo);
        request.setToken(token);
        request.setAccount(account);
        return request;
    }

    public CommonUserRequestBuilder withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public CommonUserRequestBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public CommonUserRequestBuilder withAccount(String account) {
        this.account = account;
        return this;
    }

    public CommonUserRequestBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public CommonUserRequestBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public CommonUserRequestBuilder withSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public CommonUserRequestBuilder withUserInfoBO(UserInfoBO userInfoBO) {
        this.userInfoBO = userInfoBO;
        return this;
    }

    public CommonUserRequestBuilder withRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
        return this;
    }

    public CommonUserRequestBuilder withPermIds(List<String> permIds) {
        this.permIds = permIds;
        return this;
    }

    public CommonUserRequestBuilder withExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
        return this;
    }

    public CommonUserRequestBuilder withToken(String token) {
        this.token = token;
        return this;
    }
}
