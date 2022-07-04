package com.mengtu.kaichi.organization.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * 组织关系实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:35
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization_relation",
        indexes = {
                @Index(name = "uk_organization_relation_id", columnList = "organization_relation_id", unique = true),
                @Index(name = "uk_primary_organization_id_sub_organization_id", columnList = "primary_organization_id, sub_organization_id", unique = true)
        })
public class OrganizationRelationDO extends BaseDO {

    private static final long serialVersionUID = -1816703958067952007L;

    /**
     * 组织关系id
     */
    @Column(name = "organization_relation_id", length = 32, nullable = false)
    private String organizationRelationId;

    /**
     * 主组织id
     */
    @Column(name = "primary_organization_id", length = 32, nullable = false)
    private String primaryOrganizationId;

    /**
     * 子组织id
     */
    @Column(name = "sub_organization_id", length = 32, nullable = false)
    private String subOrganizationId;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getOrganizationRelationId() {
        return organizationRelationId;
    }

    public void setOrganizationRelationId(String organizationRelationId) {
        this.organizationRelationId = organizationRelationId;
    }

    public String getPrimaryOrganizationId() {
        return primaryOrganizationId;
    }

    public void setPrimaryOrganizationId(String primaryOrganizationId) {
        this.primaryOrganizationId = primaryOrganizationId;
    }

    public String getSubOrganizationId() {
        return subOrganizationId;
    }

    public void setSubOrganizationId(String subOrganizationId) {
        this.subOrganizationId = subOrganizationId;
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