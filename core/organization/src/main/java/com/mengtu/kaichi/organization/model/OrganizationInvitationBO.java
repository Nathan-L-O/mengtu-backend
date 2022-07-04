package com.mengtu.kaichi.organization.model;

import com.mengtu.util.common.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 组织关系模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:46
 */
public class OrganizationInvitationBO extends ToString {


    /**
     * 组织邀请 ID
     */
    private String organizationInvitationId;

    /**
     * 主组织 ID
     */
    private String organizationId;

    /**
     * 成员 id
     */
    private String memberId;

    /**
     * 生命周期
     */
    private Date lifetime;

    /**
     * 编码 URL
     */
    private String encodeUrl;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

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

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}