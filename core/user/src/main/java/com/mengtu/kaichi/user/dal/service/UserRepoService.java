package com.mengtu.kaichi.user.dal.service;

import com.mengtu.kaichi.user.model.basic.perm.UserBO;

/**
 * 用户仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:35
 */
public interface UserRepoService {

    /**
     * 通过用户名获取用户
     *
     * @param userName
     * @return
     */
    UserBO queryByUserName(String userName);

    /**
     * 通过用户id获取用户
     *
     * @param userId
     * @return
     */
    UserBO queryByUserId(String userId);

    /**
     * 更新用户信息
     *
     * @param userBO
     * @return
     */
    UserBO updateUserByUserId(UserBO userBO);

    /**
     * 清除 登陆信息
     *
     * @param userId
     * @return
     */
    UserBO clearSessionId(String userId);

    /**
     * 通过 sessionId 获取用户信息
     *
     * @param sessionId
     * @return
     */
    UserBO queryBySessionId(String sessionId);

    /**
     * 创建新用户
     *
     * @param userBO
     * @return
     */
    UserBO createUser(UserBO userBO);

}
