package com.mengtu.kaichi.user.idfactory;

/**
 * 业务 ID 工厂
 * 生成 32 位业务 ID
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:36
 */
public interface BizIdFactory {

    /**
     * 生成用户 ID
     *
     * @return
     */
    String getUserId();

    /**
     * 生成用户信息 ID
     *
     * @param userId
     * @return
     */
    String getUserInfoId(String userId);

    /**
     * 生成角色 ID
     *
     * @return
     */
    String getRoleId();

    /**
     * 生成用户角色关联关系 ID
     *
     * @param roleId
     * @param userId
     * @return
     */
    String getRoleUserRelationId(String roleId, String userId);

    /**
     * 生成权限 ID
     *
     * @return
     */
    String getPermId();

    /**
     * 生成角色权限关联关系 ID
     *
     * @param roleId
     * @param permId
     * @return
     */
    String getRolePermRelationId(String roleId, String permId);

    /**
     * 生成用户权限关联关系 ID
     *
     * @param userId
     * @param permId
     * @return
     */
    String getUserPermRelationId(String userId, String permId);

}
