package com.mengtu.kaichi.user.manager;

import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.request.UserManageRequest;


/**
 * 用户管理器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/22 15:31
 */
public interface UserManager {

    /**
     * 创建用户
     *
     * @param request
     * @return
     */
    CommonUser create(UserManageRequest request);

    /**
     * 给用户批量添加角色
     *
     * @param request
     */
    void batchBindRole(UserManageRequest request);

    /**
     * 给用户批量添加角色
     *
     * @param request
     */
    void batchBindRolByCode(UserManageRequest request);

    /**
     * 给用户批量删除角色
     *
     * @param request
     */
    void batchUnbindRole(UserManageRequest request);

    /**
     * 给用户批量添加权限
     *
     * @param request
     */
    void batchBindPerm(UserManageRequest request);

    /**
     * 给用户批量删除权限
     *
     * @param request
     */
    void batchUnbindPerm(UserManageRequest request);

}
