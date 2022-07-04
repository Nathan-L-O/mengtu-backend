package com.mengtu.kaichi.linkpoint.dal.service;

import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;

import java.util.List;

/**
 * linkpoint 项目版本仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:41
 */
public interface LinkpointProjectVersionRepoService {

    /**
     * 创建项目版本
     *
     * @param projectVersionBO
     * @return
     */
    ProjectVersionBO createProjectVersion(ProjectVersionBO projectVersionBO);

    /**
     * 查询项目版本
     *
     * @param projectId
     * @return
     */
    List<ProjectVersionBO> queryByProjectId(String projectId);

    /**
     * 通过项目版本 ID 查找
     *
     * @param projectVersionId
     * @return
     */
    ProjectVersionBO queryByProjectVersionId(String projectVersionId);

    /**
     * 删除版本
     *
     * @param projectVersionId
     */
    void deleteProjectVersion(String projectVersionId);

}
