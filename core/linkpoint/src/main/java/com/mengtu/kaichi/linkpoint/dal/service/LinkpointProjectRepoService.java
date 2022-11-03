package com.mengtu.kaichi.linkpoint.dal.service;

import com.mengtu.kaichi.linkpoint.model.ProjectBO;

import java.util.List;

/**
 * linkpoint 项目仓储服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:41
 */
public interface LinkpointProjectRepoService {

    /**
     * 创建项目
     *
     * @param projectBO
     * @return
     */
    ProjectBO createProject(ProjectBO projectBO);

    /**
     * 更新项目
     *
     * @param projectBO
     * @return
     */
    ProjectBO updateProject(ProjectBO projectBO);

    /**
     * 查找项目
     *
     * @param projectBO
     * @return
     */
    ProjectBO queryProject(ProjectBO projectBO);

    /**
     * 查找所有项目
     *
     * @param projectBO
     * @return
     */
    List<ProjectBO> queryAllProject(ProjectBO projectBO);


    List<ProjectBO> queryByFolderId(String queryByFolderId);

}
