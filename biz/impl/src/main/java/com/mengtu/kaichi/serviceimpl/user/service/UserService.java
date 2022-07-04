package com.mengtu.kaichi.serviceimpl.user.service;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.request.CommonUserRequest;
import com.mengtu.kaichi.user.model.CommonUser;

import java.io.File;

/**
 * 用户服务
 *
 * @author 过昊天
 * @version 1.3 @ 2022/4/22 15:00
 */
public interface UserService {

    /**
     * 登录
     *
     * @param request
     * @param context
     * @return
     */
    CommonUser login(CommonUserRequest request, OperateContext context);

    /**
     * 通过短信验证码登录
     *
     * @param request
     * @param context
     * @return
     */
    CommonUser smsLogin(CommonUserRequest request, OperateContext context);

    /**
     * 登陆 by token
     *
     * @param request
     * @param context
     * @return
     */
    CommonUser fetchUser(CommonUserRequest request, OperateContext context);

    /**
     * 注销登录
     *
     * @param request
     * @param context
     * @return
     */
    void logout(CommonUserRequest request, OperateContext context);

    /**
     * 修改账号信息
     *
     * @param request
     * @param context
     */
    void modifyUser(CommonUserRequest request, OperateContext context);

    /**
     * 修改用户昵称
     *
     * @param request
     * @param context
     */
    void updateUserInfo(CommonUserRequest request, OperateContext context);

    /**
     * 重置密码
     *
     * @param request
     * @param context
     */
    void resetPassword(CommonUserRequest request, OperateContext context);

    /**
     * 用户注册
     *
     * @param request
     * @param context
     * @return
     */
    CommonUser register(CommonUserRequest request, OperateContext context);

    /**
     * 用户注册校验
     *
     * @param request
     */
    void registerValidate(CommonUserRequest request);

    /**
     * 注入头像签名 URL
     *
     * @param commonUser
     * @return
     */
    CommonUser injectAvatarUrl(CommonUser commonUser);

    /**
     * 头像更新
     *
     * @param request
     * @param avatarFile
     */
    void avatarUpdate(CommonUserRequest request, File avatarFile);
}

