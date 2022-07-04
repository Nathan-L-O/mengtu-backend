package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.kaichi.user.enums.RoleCode;
import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:38
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class RoleBO extends ToString {

    private static final long serialVersionUID = -2713830136923587723L;

    /**
     * 权限 ID
     */
    private String roleId;

    /**
     * 角色码
     *
     * @see RoleCode
     */
    @NotBlank
    private String roleCode;

    /**
     * 角色名称
     */
    @NotBlank
    private String roleName;

    /**
     * 角色描述
     */
    private String roleDesc;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
}
