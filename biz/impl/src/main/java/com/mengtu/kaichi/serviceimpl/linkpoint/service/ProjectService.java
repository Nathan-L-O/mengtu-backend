package com.mengtu.kaichi.serviceimpl.linkpoint.service;

import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.serviceimpl.linkpoint.request.ProjectRequest;

import java.util.List;

/**
 * 项目管理服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:36
 */
public interface ProjectService {
    /**
     * 创建项目服务
     *
     * @param request
     * @return
     */
    ProjectBO create(ProjectRequest request);

    /**
     * 更新项目服务
     *
     * @param request
     * @return
     */
    ProjectBO update(ProjectRequest request);

    /**
     * 查找项目服务
     *
     * @param request
     * @return
     */
    ProjectBO query(ProjectRequest request);

    /**
     * 查找全部项目服务
     *
     * @param request
     * @return
     */
    List<ProjectBO> queryAll(ProjectRequest request);

    /**
     * 添加创建人信息
     *
     * @param projectBOList
     * @return
     */
    List<ProjectBO> attachCreatorInfo(List<ProjectBO> projectBOList);

    /**
     * 变更归档状态
     *
     * @param request
     */
    void archive(ProjectRequest request);
}