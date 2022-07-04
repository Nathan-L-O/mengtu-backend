package com.mengtu.kaichi.serviceimpl.organization.service;

import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;

import java.io.File;

/**
 * 组织管理器
 * 鉴权逻辑
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:35
 */
public interface OrganizationManagerService {

    /**
     * 创建组织服务
     *
     * @param request
     * @return
     */
    OrganizationBO create(OrganizationRequest request);

    /**
     * 解散组织
     *
     * @param request
     */
    void disband(OrganizationRequest request);

    /**
     * 总管管理成员
     *
     * @param request
     */
    void manageMember(OrganizationRequest request);

    /**
     * 主管 管理成员身份
     *
     * @param request
     */
    void changeMemberType(OrganizationRequest request);

    /**
     * 添加成员
     *
     * @param request
     */
    void addMember(OrganizationRequest request);

    /**
     * 移除成员 管理员只能移除member
     *
     * @param request
     */
    void removeMember(OrganizationRequest request);

    /**
     * 更新头像
     *
     * @param request
     * @param avatarFile
     */
    void avatarUpdate(OrganizationRequest request, File avatarFile);

    /**
     * 更新信息
     *
     * @param request
     */
    void updateInfo(OrganizationRequest request);
}