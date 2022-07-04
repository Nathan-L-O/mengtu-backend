package com.mengtu.kaichi.user.request;

import com.mengtu.kaichi.user.enums.RoleCode;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import org.hibernate.validator.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/4/24 13:44
 */
public class UserManageRequest extends BaseRequest {

    private static final long serialVersionUID = -829974546955001782L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 明文密码
     */
    @NotBlank
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 用户名
     */
    @NotBlank
    private String username;

    /**
     * 用户信息
     */
    private UserInfoBO userInfoBO;

    /**
     * 角色 ID
     */
    private List<String> roleIds = new ArrayList<>();

    /**
     * 权限 ID
     */
    private List<String> permIds = new ArrayList<>();

    /**
     * 权限码
     */
    private RoleCode roleCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserInfoBO getUserInfoBO() {
        return userInfoBO;
    }

    public void setUserInfoBO(UserInfoBO userInfoBO) {
        this.userInfoBO = userInfoBO;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public List<String> getPermIds() {
        return permIds;
    }

    public void setPermIds(List<String> permIds) {
        this.permIds = permIds;
    }

    public RoleCode getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(RoleCode roleCode) {
        this.roleCode = roleCode;
    }
}
