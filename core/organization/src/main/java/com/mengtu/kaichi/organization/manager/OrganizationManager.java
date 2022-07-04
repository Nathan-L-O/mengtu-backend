package com.mengtu.kaichi.organization.manager;

import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.organization.request.OrganizationManageRequest;

import java.util.List;

/**
 * 组织管理器
 *
 * @author 过昊天
 * @version 1.2 @ 2022/5/10 13:37
 */
public interface OrganizationManager {

    /**
     * 创建组织
     *
     * @param request
     * @return
     */
    OrganizationBO createOrganization(OrganizationManageRequest request);

    /**
     * 解散组织
     *
     * @param request
     * @return
     */
    void disbandOrganization(OrganizationManageRequest request);

    /**
     * 管理成员
     *
     * @param request
     */
    void manageMember(OrganizationManageRequest request);

    /**
     * 查询用户所在的所有组织
     *
     * @param memberId
     * @return
     */
    List<OrganizationBO> queryOrganizationByMemberId(String memberId);

    /**
     * 查询用户所在所有组织及关系
     *
     * @param memberId
     * @return
     */
    List<OrganizationMemberBO> queryOrganizationMemberByMemberId(String memberId);

    /**
     * 查询所有组织
     *
     * @return
     */
    List<OrganizationBO> queryAllOrganization();
}