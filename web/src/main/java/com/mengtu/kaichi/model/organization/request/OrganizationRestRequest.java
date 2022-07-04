package com.mengtu.kaichi.model.organization.request;

import com.mengtu.kaichi.common.BaseRestRequest;

/**
 * 组织请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/25 13:49
 */
public class OrganizationRestRequest extends BaseRestRequest {

    private static final long serialVersionUID = -1523375024702123374L;

    /**
     * 组织id
     */
    private String organizationId;

    /**
     * 组织名
     */
    private String organizationName;

    /**
     * 组织类型
     */
    private String organizationType;

    /**
     * 成员 ID
     */
    private String attachUserId;

    /**
     * 成员id
     */
    private String memberId;

    /**
     * 成员身份
     */
    private String memberType;

    /**
     * 成员称呼
     */
    private String memberDesc;

    /**
     * 主组织id
     */
    private String primaryOrganizationId;

    /**
     * 组织邀请 ID
     */
    private String organizationInvitationId;

    /**
     * 编码 URL
     */
    private String encodeUrl;

    /**
     * 组织邀请生命周期
     */
    private String lifetime;

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

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getMemberDesc() {
        return memberDesc;
    }

    public void setMemberDesc(String memberDesc) {
        this.memberDesc = memberDesc;
    }

    public String getPrimaryOrganizationId() {
        return primaryOrganizationId;
    }

    public void setPrimaryOrganizationId(String primaryOrganizationId) {
        this.primaryOrganizationId = primaryOrganizationId;
    }

    public String getAttachUserId() {
        return attachUserId;
    }

    public void setAttachUserId(String attachUserId) {
        this.attachUserId = attachUserId;
    }

    public String getOrganizationInvitationId() {
        return organizationInvitationId;
    }

    public void setOrganizationInvitationId(String organizationInvitationId) {
        this.organizationInvitationId = organizationInvitationId;
    }

    public String getEncodeUrl() {
        return encodeUrl;
    }

    public void setEncodeUrl(String encodeUrl) {
        this.encodeUrl = encodeUrl;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String toAuditString() {
        return organizationName + organizationType + memberDesc + memberType + encodeUrl;
    }
}
