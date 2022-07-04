package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色权限映射关系
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:41
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class RolePermRelationBO extends ToString {

    private static final long serialVersionUID = 1648212858135385465L;

    /**
     * 角色权限映射 ID
     */
    private String rolePermId;

    /**
     * 角色 ID
     */
    @NotBlank
    private String roleId;

    /**
     * 权限 ID
     */
    @NotBlank
    private String permId;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public String getRolePermId() {
        return rolePermId;
    }

    public void setRolePermId(String rolePermId) {
        this.rolePermId = rolePermId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }
}
