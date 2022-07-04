package com.mengtu.kaichi.serviceimpl.user.service;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.model.basic.perm.UserRoleRelationBO;

import java.util.List;

/**
 * 角色服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 11:19
 */
public interface RoleService {


    /**
     * 获取所有用户角色
     *
     * @return
     */
    List<RoleBO> findAllRole();

    /**
     * 创建角色
     *
     * @param request
     * @param context
     * @return
     */
    RoleBO createRole(RoleUserPermRequest request, OperateContext context);

    /**
     * 绑定用户
     *
     * @param request
     * @param context
     * @return
     */
    void bindUsers(RoleUserPermRequest request, OperateContext context);

    /**
     * 解绑用户
     *
     * @param request
     * @param context
     * @return
     */
    void unbindUsers(RoleUserPermRequest request, OperateContext context);


    /**
     * 获取所有用户以及所有用户的角色
     *
     * @return
     */
    List<UserRoleRelationBO> findAllUserRelationRole();

}
