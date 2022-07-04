package com.mengtu.kaichi.organization.dal.service;


import com.mengtu.kaichi.organization.model.OrganizationMemberBO;

import java.util.List;


/**
 * 组织成员仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:37
 */
public interface OrganizationMemberRepoService {

    /**
     * 添加成员
     *
     * @param memberBO
     */
    void addMember(OrganizationMemberBO memberBO);

    /**
     * 移除成员
     *
     * @param organizationId
     * @param memberId
     */
    void removeMember(String organizationId, String memberId);

    /**
     * 修改成员信息
     *
     * @param memberBO
     * @return
     */
    OrganizationMemberBO updateMember(OrganizationMemberBO memberBO);

    /**
     * 查询成员
     *
     * @param organizationId
     * @param memberType
     * @return
     */
    List<OrganizationMemberBO> queryMembers(String organizationId, String memberType);

    /**
     * 查询主管
     *
     * @param organizationId
     * @return
     */
    OrganizationMemberBO queryPrincipal(String organizationId);

    /**
     * 查询成员
     *
     * @param organizationId
     * @param memberId
     * @return
     */
    OrganizationMemberBO queryMember(String organizationId, String memberId);

    /**
     * 查询成员组织
     *
     * @param memberId
     * @return
     */
    List<OrganizationMemberBO> queryOrganizations(String memberId);


    /**
     * 解散组织
     *
     * @param organizationId
     */
    void disband(String organizationId);
}