package com.mengtu.kaichi.user.dal.model.perm;

import com.mengtu.kaichi.user.dal.model.BaseDO;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 角色权限映射关系
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 17:47
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "common_role_perm_relation",
        indexes = {
                @Index(name = "uk_role_perm_id", columnList = "role_perm_id", unique = true),
                @Index(name = "uk_role_id_perm_id", columnList = "role_id, perm_id", unique = true)
        })
public class RolePermRelationDO extends BaseDO {

    private static final long serialVersionUID = -4171561993007901760L;

    /**
     * 角色权限映射id
     */
    @Column(name = "role_perm_id", length = 32, nullable = false, updatable = false)
    private String rolePermId;

    /**
     * 角色id
     */
    @Column(name = "role_id", length = 32, nullable = false)
    private String roleId;

    /**
     * 权限id
     */
    @Column(name = "perm_id", length = 32, nullable = false)
    private String permId;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

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

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
