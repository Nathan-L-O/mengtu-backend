package com.mengtu.kaichi.organization.dal.service;

import com.mengtu.kaichi.organization.model.OrganizationBO;

import java.util.List;

/**
 * 组织仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:37
 */
public interface OrganizationRepoService {

    /**
     * 创建组织
     *
     * @param organizationBO
     * @return
     */
    OrganizationBO create(OrganizationBO organizationBO);

    /**
     * 删除组织
     *
     * @param organizationId
     * @return
     */
    void disband(String organizationId);

    /**
     * 组织修改
     *
     * @param organizationBO
     * @return
     */
    OrganizationBO modify(OrganizationBO organizationBO);

    /**
     * 获取全部组织
     *
     * @return
     */
    List<OrganizationBO> queryAllOrganization();

    /**
     * 通过id获取组织
     *
     * @param organizationId
     * @return
     */
    OrganizationBO queryByOrganizationId(String organizationId);

    /**
     * 查询组织
     *
     * @param organizationId
     * @return
     */
    List<OrganizationBO> queryByOrganizationIds(List<String> organizationId);

    /**
     * 查询组织 通过组织名称
     *
     * @param organizationName
     * @return
     */
    OrganizationBO queryByOrganizationName(String organizationName);
}