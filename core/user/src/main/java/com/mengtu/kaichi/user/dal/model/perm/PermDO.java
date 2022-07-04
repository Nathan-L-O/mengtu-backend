package com.mengtu.kaichi.user.dal.model.perm;

import com.mengtu.kaichi.user.dal.model.BaseDO;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 权限实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 17:42
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "common_perm",
        indexes = {
                @Index(name = "uk_perm_id", columnList = "perm_id", unique = true),
                @Index(name = "idx_perm_id_perm_type", columnList = "perm_id, perm_type", unique = false)
        })
public class PermDO extends BaseDO {

    private static final long serialVersionUID = 1861693142222734590L;

    /**
     * 权限id
     */
    @Column(name = "perm_id", length = 32, updatable = false, nullable = false)
    private String permId;

    /**
     * 权限类型
     */
    @Column(name = "perm_type", length = 64, updatable = false, nullable = false)
    private String permType;

    /**
     * 权限名称
     */
    @Column(name = "perm_name", nullable = false)
    private String permName;

    /**
     * 权限描述
     */
    @Column(length = 400)
    private String permDesc;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermDesc() {
        return permDesc;
    }

    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
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
