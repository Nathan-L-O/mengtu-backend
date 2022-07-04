package com.mengtu.kaichi.serviceimpl.organization.service;

import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;

import java.util.List;

/**
 * 组织管理服务
 * 无需鉴权
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:36
 * @return null
 */
public interface OrganizationService {

    /**
     * 创建组织服务
     *
     * @param request
     * @return
     */
    OrganizationBO create(OrganizationRequest request);

    /**
     * 成员管理
     *
     * @param request
     */
    void memberManage(OrganizationRequest request);

    /**
     * 更细信息
     *
     * @param request
     */
    void update(OrganizationRequest request);

    /**
     * 成员移除
     *
     * @param request
     */
    void removeMember(OrganizationRequest request);

    /**
     * 解散组织
     *
     * @param request
     */
    void disband(OrganizationRequest request);

    /**
     * 查询用户所在所有组织及关系
     *
     * @param request
     * @return
     */
    List<OrganizationMemberBO> queryOrganizationMemberByMemberId(OrganizationRequest request);

    /**
     * 查询组织成员
     *
     * @param request
     * @return
     */
    List<List<OrganizationMemberBO>> queryOrganizationMemberByOrganizationId(OrganizationRequest request);
}