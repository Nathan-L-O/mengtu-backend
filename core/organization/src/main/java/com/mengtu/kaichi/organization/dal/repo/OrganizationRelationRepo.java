package com.mengtu.kaichi.organization.dal.repo;

import com.mengtu.kaichi.organization.dal.model.OrganizationRelationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 组织关系仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:36
 */
@Repository
public interface OrganizationRelationRepo extends JpaRepository<OrganizationRelationDO, Long> {

    /**
     * 通过主组织id和子组织id获取
     *
     * @param primaryOrganizationId
     * @param subOrganizationId
     * @return
     */
    OrganizationRelationDO findByPrimaryOrganizationIdAndSubOrganizationId(String primaryOrganizationId, String subOrganizationId);

    /**
     * 解除相关组织关系
     *
     * @param primaryOrganizationId
     */
    void deleteByPrimaryOrganizationId(String primaryOrganizationId);

    /**
     * 删除相关组织关系
     *
     * @param subOrganizationId
     */
    void deleteBySubOrganizationId(String subOrganizationId);
}