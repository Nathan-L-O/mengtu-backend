/**
 * betahouse.us
 * CopyRight (c) 2012 - 2019
 */
package com.mengtu.kaichi.organization.dal.repo;

import com.mengtu.kaichi.organization.dal.model.OrganizationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 组织仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:36
 */
@Repository
public interface OrganizationRepo extends JpaRepository<OrganizationDO, Long> {

    /**
     * 查询组织id
     *
     * @param organizationId
     * @return
     */
    OrganizationDO findByOrganizationId(String organizationId);

    /**
     * 查询组织名称
     *
     * @param organizationName
     * @return
     */
    OrganizationDO findByOrganizationName(String organizationName);

    /**
     * 查询存在的组织
     *
     * @param organizationIds
     * @return
     */
    List<OrganizationDO> findAllByOrganizationIdIn(List<String> organizationIds);

    /**
     * 删除组织
     *
     * @param organizationId
     */
    void deleteByOrganizationId(String organizationId);

}