package com.mengtu.kaichi.user.dal.repo.perm;

import com.mengtu.kaichi.user.dal.model.perm.RoleDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:53
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface RoleDORepo extends JpaRepository<RoleDO, String> {

    /**
     * 获取角色实体
     *
     * @param roleId
     * @return
     */
    RoleDO findByRoleId(String roleId);

    /**
     * 批量获取角色实体
     *
     * @param roleIds
     * @return
     */
    List<RoleDO> findAllByRoleIdIn(List<String> roleIds);

    /**
     * 通过角色号获取角色
     *
     * @param roleCode
     * @return
     */
    RoleDO findByRoleCode(String roleCode);
}