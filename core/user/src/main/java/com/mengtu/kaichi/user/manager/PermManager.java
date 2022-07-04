package com.mengtu.kaichi.user.manager;

import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.request.PermManageRequest;

import java.util.List;

/**
 * 权限管理器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:38
 */
public interface PermManager {


    /**
     * 创建权限
     *
     * @param request
     * @return
     */
    PermBO createPerm(PermManageRequest request);


    /**
     * 批量用户绑定权限
     *
     * @param request
     */
    void batchUsersBindPerms(PermManageRequest request);

    /**
     * 批量用户解绑权限
     *
     * @param request
     */
    void batchUsersUnbindPerms(PermManageRequest request);

    /**
     * 和所有用户解绑
     *
     * @param permId
     */
    void detachAllUsers(String permId);

    /**
     * 获取权限的用户id 并按照创建时间顺序排序
     *
     * @param permId
     * @return
     */
    List<String> getPermUsers(String permId);
}
