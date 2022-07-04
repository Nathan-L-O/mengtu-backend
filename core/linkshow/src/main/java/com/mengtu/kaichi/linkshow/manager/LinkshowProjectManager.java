package com.mengtu.kaichi.linkshow.manager;


import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;
import com.mengtu.kaichi.linkshow.request.ProjectManageRequest;

import java.io.File;
import java.util.List;

/**
 * 项目管理器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 13:38
 */
public interface LinkshowProjectManager {

    /**
     * 创建项目
     *
     * @param projectManageRequest
     * @return
     */
    ProjectBO createProject(ProjectManageRequest projectManageRequest);


    /**
     * 更新项目
     *
     * @param projectManageRequest
     * @return
     */
    ProjectBO updateProject(ProjectManageRequest projectManageRequest);

    /**
     * 归档变更
     *
     * @param projectManageRequest
     */
    void archive(ProjectManageRequest projectManageRequest);

    /**
     * 查询版本列表
     *
     * @param projectManageRequest
     * @return
     */
    List<ProjectVersionBO> queryVersionList(ProjectManageRequest projectManageRequest);

    /**
     * 复制版本
     *
     * @param request
     * @return
     */
    ProjectBO duplicateVersion(ProjectManageRequest request);

    /**
     * 上传数据
     *
     * @param request
     * @param file
     * @param preview
     * @return
     */
    ProjectVersionBO uploadData(ProjectManageRequest request, File file, File preview);

    /**
     * 版本检出
     *
     * @param request
     * @return
     */
    ProjectVersionBO checkoutVersion(ProjectManageRequest request);

    /**
     * 版本删除
     *
     * @param request
     */
    void deleteAllVersion(ProjectManageRequest request);

}