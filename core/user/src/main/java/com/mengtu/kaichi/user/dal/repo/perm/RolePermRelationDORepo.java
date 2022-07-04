package com.mengtu.kaichi.user.dal.repo.perm;

import com.mengtu.kaichi.user.dal.model.perm.RolePermRelationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限映射仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:54
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface RolePermRelationDORepo extends JpaRepository<RolePermRelationDO, Long> {

    /**
     * 通过角色获取权限映射
     *
     * @param roleId
     * @return
     */
    List<RolePermRelationDO> findAllByRoleId(String roleId);

    /**
     * 通过角色获取权限映射
     *
     * @param roleIds
     * @return
     */
    List<RolePermRelationDO> findAllByRoleIdIn(List<String> roleIds);

    /**
     * 批量解除角色上的权限
     *
     * @param role
     * @param permIds
     */
    void deleteByRoleIdAndPermIdIn(String role, List<String> permIds);

    /**
     * 解除权限的全部角色上的绑定
     *
     * @param permId
     */
    void deleteByPermId(String permId);

    /**
     * 获取角色对应的权限
     *
     * @param roleId
     * @param permId
     * @return
     */
    RolePermRelationDO findByRoleIdAndPermId(String roleId, String permId);
}
