package com.mengtu.kaichi.user.dal.repo.perm;

import com.mengtu.kaichi.user.dal.model.perm.UserRoleRelationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关系仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:50
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface UserRoleRelationDORepo extends JpaRepository<UserRoleRelationDO, Long> {

    /**
     * 查询用户的所有角色
     *
     * @param userId
     * @return
     */
    List<UserRoleRelationDO> findAllByUserId(String userId);


    /**
     * 解绑用户下的角色
     *
     * @param userId
     * @param roleIds
     */
    void deleteByUserIdAndRoleIdIn(String userId, List<String> roleIds);

    /**
     * 查询角色下的所有用户
     *
     * @param roleId
     * @return
     */
    List<UserRoleRelationDO> findAllByRoleId(String roleId);

    /**
     * 批量解绑用户
     *
     * @param roleId
     * @param userIds
     */
    void deleteByRoleIdAndUserIdIn(String roleId, List<String> userIds);

    /**
     * 删除所有用户和权限的关联
     *
     * @param roleId
     */
    void deleteByRoleId(String roleId);

}
