package com.mengtu.kaichi.serviceimpl.organization.service;

import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;

/**
 * 组织邀请服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/6 14:09
 */
public interface OrganizationInvitationService {

    /**
     * 创建邀请
     *
     * @param request
     * @return
     */
    OrganizationInvitationBO createInvitation(OrganizationRequest request);

    /**
     * 查询个人邀请
     *
     * @param request
     * @return
     */
    OrganizationInvitationBO queryInvitation(OrganizationRequest request);

    /**
     * 反查邀请
     *
     * @param request
     * @return
     */
    OrganizationInvitationBO getInvitation(OrganizationRequest request);

    /**
     * 接收邀请
     *
     * @param request
     * @return
     */
    void acceptInvitation(OrganizationRequest request);

}