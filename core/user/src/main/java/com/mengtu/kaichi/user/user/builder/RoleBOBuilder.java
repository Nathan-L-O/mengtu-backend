package com.mengtu.kaichi.user.user.builder;

import com.mengtu.kaichi.user.model.basic.perm.RoleBO;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色构建者
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:42
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class RoleBOBuilder {

    /**
     * 角色码
     */
    private String roleCode;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public static RoleBOBuilder getInstance(String roleCode, String roleName) {
        return new RoleBOBuilder(roleCode, roleName);
    }

    public static RoleBOBuilder getInstance() {
        return new RoleBOBuilder();
    }

    public RoleBO build() {
        RoleBO roleBO = new RoleBO();
        roleBO.setRoleCode(roleCode);
        roleBO.setRoleName(roleName);
        roleBO.setRoleDesc(roleDesc);
        if (extInfo != null) {
            roleBO.setExtInfo(extInfo);
        }
        return roleBO;
    }

    private RoleBOBuilder() {
    }

    private RoleBOBuilder(String roleCode, String roleName) {
        this.roleCode = roleCode;
        this.roleName = roleName;
    }

    public RoleBOBuilder withRoleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public RoleBOBuilder withRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
        return this;
    }

    public RoleBOBuilder withRoleCode(String roleCode) {
        this.roleCode = roleCode;
        return this;
    }

    public RoleBOBuilder withExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
        return this;
    }
}
