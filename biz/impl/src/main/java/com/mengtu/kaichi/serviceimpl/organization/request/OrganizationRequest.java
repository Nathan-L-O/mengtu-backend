package com.mengtu.kaichi.serviceimpl.organization.request;

import com.mengtu.kaichi.organization.request.OrganizationManageRequest;
import com.mengtu.kaichi.serviceimpl.common.verify.VerifyRequest;

import java.util.Date;

/**
 * 组织请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:34
 */
public class OrganizationRequest extends OrganizationManageRequest implements VerifyRequest {

    private static final long serialVersionUID = 5376831561117473340L;

    /**
     * 操作员id
     */
    private String userId;

    /**
     * 管理用户 ID
     */
    private String attachUserId;

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
    private Date lifetime;


    public String getAttachUserId() {
        return attachUserId;
    }

    public void setAttachUserId(String attachUserId) {
        this.attachUserId = attachUserId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getLifetime() {
        return lifetime;
    }

    public void setLifetime(Date lifetime) {
        this.lifetime = lifetime;
    }

    @Override
    public String getVerifyUserId() {
        return userId;
    }
}