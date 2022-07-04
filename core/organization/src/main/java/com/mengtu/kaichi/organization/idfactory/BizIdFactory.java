package com.mengtu.kaichi.organization.idfactory;

/**
 * 业务 id 工厂
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:38
 */
public interface BizIdFactory {

    /**
     * 生成组织id
     *
     * @return
     */
    String getOrganizationId();

    /**
     * 生成组织成员id
     *
     * @param organizationId
     * @param memberId
     * @return
     */
    String getOrganizationMemberId(String organizationId, String memberId);

    /**
     * 生成组织关系id
     *
     * @param primaryOrganizationId
     * @param subOrganizationId
     * @return
     */
    String getOrganizationRelation(String primaryOrganizationId, String subOrganizationId);

    /**
     * 生成组织邀请id
     *
     * @param organizationId
     * @param memberId
     * @return
     */
    String getOrganizationInvitation(String organizationId, String memberId);


}
