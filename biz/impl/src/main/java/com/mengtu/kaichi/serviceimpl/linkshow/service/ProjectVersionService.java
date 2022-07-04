package com.mengtu.kaichi.serviceimpl.linkshow.service;

import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;
import com.mengtu.kaichi.serviceimpl.linkshow.request.ProjectRequest;

import java.io.File;
import java.util.List;

/**
 * 项目版本管理服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/11 14:38
 */
public interface ProjectVersionService {

    /**
     * 版本信息查询
     *
     * @param request
     * @return
     */
    List<ProjectVersionBO> queryVersionList(ProjectRequest request);

    /**
     * 添加操作员名称
     *
     * @param projectVersionBOList
     * @return
     */
    List<ProjectVersionBO> attachOperatorName(List<ProjectVersionBO> projectVersionBOList);

    /**
     * 复制版本
     *
     * @param request
     * @return
     */
    ProjectBO duplicateVersion(ProjectRequest request);

    /**
     * 检出
     *
     * @param request
     * @return
     */
    ProjectVersionBO versionCheckout(ProjectRequest request);

    /**
     * 上传文件
     *
     * @param request
     * @param file
     * @param preview
     * @return
     */
    ProjectVersionBO uploadData(ProjectRequest request, File file, File preview);
}