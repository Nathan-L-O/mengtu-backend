package com.mengtu.kaichi.model.linkpoint.request;

import com.mengtu.kaichi.common.BaseRestRequest;

/**
 * 项目请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/25 13:44
 */
public class ProjectRestRequest extends BaseRestRequest {

    private static final long serialVersionUID = -5870420958739320622L;

    /**
     * 域 ID
     */
    private String domainId;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String projectDescription;

    /**
     * 项目版本 ID
     */
    private String projectVersionId;

    /**
     * 项目所在文件夹
     */
    private String folderId;


    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectVersionId() {
        return projectVersionId;
    }

    public void setProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
    }

    public String toAuditString() {
        return projectName + projectDescription;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }
}
