package com.mengtu.kaichi.model.user.request;

import com.mengtu.kaichi.common.BaseRestRequest;

import java.util.List;

/**
 * 角色请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 11:25
 */
public class RoleUserPermRestRequest extends BaseRestRequest {

    private static final long serialVersionUID = 3282884546713750137L;

    /**
     * 操作用户id
     */
    private List<String> userIds;

    /**
     * 操作用户ID
     */
    private List<String> usernames;

    /**
     * 角色名字
     */
    private String roleName;


    /**
     * 操作角色ids
     */
    private List<String> roleIds;

    /**
     * 操作角色id
     */
    private String roleId;

    /**
     * 操作权限ids
     */
    private List<String> permIds;

    /**
     * 操作权限id
     */
    private String permId;

    /**
     * 描述信息
     */
    private String describe;

    /**
     * 权限名字
     */
    private String permName;


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roldName) {
        this.roleName = roldName;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
