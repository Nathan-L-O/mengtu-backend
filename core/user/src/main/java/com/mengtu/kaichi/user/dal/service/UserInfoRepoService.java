package com.mengtu.kaichi.user.dal.service;

import com.mengtu.kaichi.user.model.basic.UserInfoBO;

/**
 * 用户信息仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:35
 */
public interface UserInfoRepoService {

    /**
     * 绑定用户信息
     *
     * @param userId     用户 ID
     * @param userInfoBO
     */
    void bindUserInfo(String userId, UserInfoBO userInfoBO);

    /**
     * 通过用户 ID 查询用户信息
     *
     * @param userId
     * @return
     */
    UserInfoBO queryUserInfoByUserId(String userId);

    /**
     * 修改用户信息
     *
     * @param userId
     * @param userInfoBO
     * @return
     */
    UserInfoBO modifyUserInfoByUserId(String userId, UserInfoBO userInfoBO);

    /**
     * createUserInfo
     *
     * @param userInfoBO
     * @return com.mengtu.kaichi.user.model.basic.UserInfoBO
     */
    UserInfoBO createUserInfo(UserInfoBO userInfoBO);

}
