package com.mengtu.kaichi.organization.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 组织实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:31
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization",
        indexes = {
                @Index(name = "uk_organization_id", columnList = "organization_id", unique = true),
                @Index(name = "uk_organization_name_id", columnList = "organization_name", unique = true)
        })
public class OrganizationDO extends BaseDO {

    private static final long serialVersionUID = -8836560775204266028L;

    /**
     * 组织id
     */
    @Column(name = "organization_id", length = 32, nullable = false)
    private String organizationId;

    /**
     * 组织名称
     */
    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    /**
     * 组织类型
     */
    @Column(name = "organization_type")
    private String organizationType;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
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