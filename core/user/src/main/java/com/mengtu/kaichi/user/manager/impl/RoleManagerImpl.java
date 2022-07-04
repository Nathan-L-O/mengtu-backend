package com.mengtu.kaichi.user.manager.impl;

import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.dal.service.RoleRepoService;
import com.mengtu.kaichi.user.manager.RoleManager;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.request.RoleManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 角色管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:54
 */
@Service
public class RoleManagerImpl implements RoleManager {

    @Resource
    private RoleRepoService roleRepoService;

    @Resource
    private PermRepoService permRepoService;

    @Override
    @Transactional
    public RoleBO createRole(RoleManageRequest request) {
        // 创建角色
        RoleBO roleBO = roleRepoService.createRole(request.getRole());

        // 绑定权限
        if (!CollectionUtil.isEmpty(request.getPermIds())) {
            permRepoService.roleBindPerms(roleBO.getRoleId(), request.getPermIds());
        }

        // 绑定用户
        if (!CollectionUtil.isEmpty(request.getUserIds())) {
            roleRepoService.usersBindRole(request.getUserIds(), roleBO.getRoleId());
        }
        return roleBO;
    }

    @Override
    public void batchRoleBindPerms(RoleManageRequest request) {
        AssertUtil.assertNotNull(request.getRole());
        AssertUtil.assertStringNotBlank(request.getRole().getRoleId());
        AssertUtil.assertNotNull(request.getPermIds());
        permRepoService.roleBindPerms(request.getRole().getRoleId(), request.getPermIds());
    }

    @Override
    public void batchRoleUnbindPerms(RoleManageRequest request) {
        AssertUtil.assertNotNull(request.getRole());
        AssertUtil.assertStringNotBlank(request.getRole().getRoleId());
        AssertUtil.assertNotNull(request.getPermIds());
        permRepoService.roleUnbindPerms(request.getRole().getRoleId(), request.getPermIds());
    }

    @Override
    public void batchUsersBindRole(RoleManageRequest request) {
        AssertUtil.assertNotNull(request.getRole());
        AssertUtil.assertStringNotBlank(request.getRole().getRoleId());
        AssertUtil.assertNotNull(request.getUserIds());
        roleRepoService.usersBindRole(request.getUserIds(), request.getRole().getRoleId());
    }

    @Override
    public void batchUsersUnbindRole(RoleManageRequest request) {
        AssertUtil.assertNotNull(request.getRole());
        AssertUtil.assertStringNotBlank(request.getRole().getRoleId());
        AssertUtil.assertNotNull(request.getUserIds());
        roleRepoService.usersUnbindRole(request.getUserIds(), request.getRole().getRoleId());
    }

    @Override
    public List<RoleBO> findAllRole() {
        return roleRepoService.findAllRole();
    }
}
