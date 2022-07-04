package com.mengtu.kaichi.organization.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 组织邀请实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/6 09:48
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "organization_invitation",
        indexes = {
                @Index(name = "uk_organization_invitation_id", columnList = "organization_invitation_id", unique = true),
                @Index(name = "uk_organization_id_member_id", columnList = "organization_id, member_id", unique = true)
        })
public class OrganizationInvitationDO extends BaseDO {

    private static final long serialVersionUID = -1816703958067952007L;

    /**
     * 组织邀请 id
     */
    @Column(name = "organization_invitation_id", length = 32, nullable = false)
    private String organizationInvitationId;

    /**
     * 主组织 id
     */
    @Column(name = "organization_id", length = 32, nullable = false)
    private String organizationId;

    /**
     * 发起成员 id
     */
    @Column(name = "member_id", length = 32, nullable = false)
    private String memberId;

    /**
     * 生命周期
     */
    @Column(name = "lifetime", nullable = false)
    private Date lifetime;

    /**
     * 编码 URL
     */
    @Column(name = "encode_url", length = 200, nullable = false)
    private String encodeUrl;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getOrganizationInvitationId() {
        return organizationInvitationId;
    }

    public void setOrganizationInvitationId(String organizationInvitationId) {
        this.organizationInvitationId = organizationInvitationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getLifetime() {
        return lifetime;
    }

    public void setLifetime(Date lifetime) {
        this.lifetime = lifetime;
    }

    public String getEncodeUrl() {
        return encodeUrl;
    }

    public void setEncodeUrl(String encodeUrl) {
        this.encodeUrl = encodeUrl;
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