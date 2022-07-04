package com.mengtu.kaichi.organization.dal.repo;

import com.mengtu.kaichi.organization.dal.model.OrganizationInvitationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织邀请仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:36
 */
@Repository
public interface OrganizationInvitationRepo extends JpaRepository<OrganizationInvitationDO, Long> {

    /**
     * 通过组织邀请 ID 获取
     *
     * @param organizationInvitationId
     * @return
     */
    OrganizationInvitationDO findByOrganizationInvitationId(String organizationInvitationId);

    /**
     * 通过组织 ID 获取
     *
     * @param organizationId
     * @return
     */
    OrganizationInvitationDO findByOrganizationId(String organizationId);


    /**
     * 通过组织 ID 及成员 ID 获取
     *
     * @param organizationId
     * @param memberId
     * @return
     */
    OrganizationInvitationDO findByOrganizationIdAndMemberId(String organizationId, String memberId);

    /**
     * 通过编码 URL 获取
     *
     * @param encodeUrl
     * @return
     */
    OrganizationInvitationDO findByEncodeUrl(String encodeUrl);

    /**
     * 全查询
     *
     * @return
     */
    List<OrganizationInvitationDO> findAll();

    /**
     * 删除 by ID
     *
     * @param organizationInvitationId
     */
    void deleteByOrganizationInvitationId(String organizationInvitationId);

}