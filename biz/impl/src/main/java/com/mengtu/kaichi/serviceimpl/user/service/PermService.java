package com.mengtu.kaichi.serviceimpl.user.service;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;

import java.util.List;

/**
 * 权限服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:12
 */
public interface PermService {

    /**
     * 创建权限
     *
     * @param request
     * @return
     */
    PermBO createPerm(RoleUserPermRequest request, OperateContext context);

    /**
     * 批量用户绑定权限
     *
     * @param request
     */
    List<UserInfoBO> batchUsersBindPerms(RoleUserPermRequest request, OperateContext context);

    /**
     * 批量用户解绑权限
     *
     * @param request
     */
    void batchUsersUnbindPerms(RoleUserPermRequest request, OperateContext context);

    /**
     * 和所有用户解绑
     *
     * @param request
     * @param context
     */
    void detachAllUsers(RoleUserPermRequest request, OperateContext context);

    /**
     * 获取权限的用户id 并按照创建时间顺序排序
     *
     * @param request
     * @param context
     * @return
     */
    List<UserInfoBO> getPermUsers(RoleUserPermRequest request, OperateContext context);


    /**
     * 获取所有权限排除 活动盖章和财务管理权限
     *
     * @return
     */
    List<PermBO> findAllNotContainStamperAndFinance();

    /**
     * 获取所有权限, 排除组织管理
     *
     * @return
     */
    List<PermBO> findAllNotContainOrganization();


    /**
     * 获取用户权限
     *
     * @return
     */
    List<PermBO> findPerm(RoleUserPermRequest request);
}
