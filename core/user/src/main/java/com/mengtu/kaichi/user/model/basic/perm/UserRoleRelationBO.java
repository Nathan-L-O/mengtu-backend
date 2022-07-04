package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.util.common.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户角色关联映射
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:42
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class UserRoleRelationBO extends ToString {

    private static final long serialVersionUID = -3746460200348083797L;

    /**
     * 用户角色映射 ID
     */
    private String userRoleId;

    /**
     * 用户 ID
     */
    @NotNull
    private String userId;

    /**
     * 角色 ID
     */
    @NotNull
    private String roleId;

    /**
     * 用户名字
     */
    private String userName;

    /**
     * 角色名字
     */
    private String roleName;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public String getUserRoleId() {
        return userRoleId;
    }

    public void setUserRoleId(String userRoleId) {
        this.userRoleId = userRoleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
