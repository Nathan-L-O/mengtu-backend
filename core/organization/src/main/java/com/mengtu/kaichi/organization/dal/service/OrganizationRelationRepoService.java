package com.mengtu.kaichi.organization.dal.service;

import com.mengtu.kaichi.organization.model.OrganizationRelationBO;

/**
 * 组织关系仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:49
 */
public interface OrganizationRelationRepoService {

    /**
     * 创建组织间关系
     *
     * @param relationBO
     * @return
     */
    OrganizationRelationBO createRelation(OrganizationRelationBO relationBO);

    /**
     * 删除组织间关系
     *
     * @param primaryOrganizationId
     * @param subOrganizationId
     * @return
     */
    void removeRelation(String primaryOrganizationId, String subOrganizationId);

    /**
     * 解除和所有组织关系
     *
     * @param organizationId
     */
    void disband(String organizationId);
}