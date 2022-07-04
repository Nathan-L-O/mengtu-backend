package com.mengtu.kaichi.organization.dal.service;

import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;

/**
 * 组织邀请仓储服务
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/6 10:39
 */
public interface OrganizationInvitationRepoService {

    /**
     * 创建组织邀请
     *
     * @param organizationInvitationBO
     * @return
     */
    void createInvitation(OrganizationInvitationBO organizationInvitationBO);

    /**
     * 更新组织邀请
     *
     * @param organizationInvitationBO
     * @return
     */
    void updateInvitation(OrganizationInvitationBO organizationInvitationBO);

    /**
     * 通过编码 URL 查找组织邀请
     *
     * @param encodeUrl
     * @return
     */
    OrganizationInvitationBO queryInvitationByUrl(String encodeUrl);

    /**
     * 通过组织 ID 及 成员 ID 查询
     *
     * @param organizationId
     * @param memberId
     * @return
     */
    OrganizationInvitationBO queryInvitationByOrganizationIdAndMemberId(String organizationId, String memberId);

    /**
     * 通过邀请 ID 查询
     *
     * @param organizationInvitationId
     * @return
     */
    OrganizationInvitationBO queryInvitationByOrganizationInvitationId(String organizationInvitationId);

    /**
     * 删除
     *
     * @param organizationInvitationId
     */
    void delete(String organizationInvitationId);
}