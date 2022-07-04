package com.mengtu.kaichi.user.manager;

import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.request.RoleManageRequest;

import java.util.List;

/**
 * 角色管理器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:37
 */
public interface RoleManager {


    /**
     * 创建角色
     *
     * @param request
     * @return
     */
    RoleBO createRole(RoleManageRequest request);

    /**
     * 给角色批量绑定权限
     *
     * @param request
     */
    void batchRoleBindPerms(RoleManageRequest request);

    /**
     * 给角色解除权限
     *
     * @param request
     */
    void batchRoleUnbindPerms(RoleManageRequest request);

    /**
     * 批量用户绑定角色
     *
     * @param request
     */
    void batchUsersBindRole(RoleManageRequest request);

    /**
     * 批量用户解绑角色
     *
     * @param request
     */
    void batchUsersUnbindRole(RoleManageRequest request);

    /**
     * 获取所有角色信息
     *
     * @return
     */
    List<RoleBO> findAllRole();
}
