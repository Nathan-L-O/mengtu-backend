package com.mengtu.kaichi.user.dal.model.perm;

import com.mengtu.kaichi.user.dal.model.BaseDO;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 角色实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 17:46
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "common_role",
        indexes = {
                @Index(name = "uk_role_id", columnList = "role_id", unique = true),
                @Index(name = "uk_role_code", columnList = "role_code", unique = true)
        })
public class RoleDO extends BaseDO {

    private static final long serialVersionUID = -2518686774672467568L;

    /**
     * 权限id
     */
    @Column(name = "role_id", length = 32, updatable = false, nullable = false)
    private String roleId;

    /**
     * 角色码
     */
    @Column(name = "role_code", nullable = false, updatable = false)
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(nullable = false)
    private String roleName;

    /**
     * 角色描述
     */
    @Column(length = 400)
    private String roleDesc;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

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

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
