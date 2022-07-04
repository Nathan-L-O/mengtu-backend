package com.mengtu.kaichi.user.model;

import com.mengtu.kaichi.user.model.basic.perm.RoleBO;

import java.util.HashMap;
import java.util.Map;

/**
 * 基础用户类型(带角色信息)
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:43
 */
public class CommonUser extends BasicUser {

    private static final long serialVersionUID = -7184047641416166739L;

    /**
     * 角色信息
     */
    private Map<String, RoleBO> roleInfo = new HashMap<>();

    /**
     * 放入角色
     *
     * @param roleBO
     */
    public void putRole(RoleBO roleBO) {
        if (roleInfo == null) {
            roleInfo = new HashMap<>();
        }
        roleInfo.put(roleBO.getRoleId(), roleBO);
    }

    /**
     * 获取角色
     *
     * @param roleId
     * @return
     */
    public RoleBO fetchRole(String roleId) {
        if (roleInfo == null) {
            return null;
        }
        return roleInfo.get(roleId);
    }

    public Map<String, RoleBO> getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(Map<String, RoleBO> roleInfo) {
        this.roleInfo = roleInfo;
    }
}
