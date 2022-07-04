package com.mengtu.kaichi.linkpoint.request;

/**
 * 项目管理请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 13:41
 */
public class ProjectManageRequest extends BaseRequest {

    private static final long serialVersionUID = -98216989525529271L;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 域 ID
     */
    private String domainId;

    /**
     * 初始化 ID
     */
    private String initialId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目描述
     */
    private String projectDescription;

    /**
     * 项目状态
     */
    private int status;

    /**
     * 归档状态
     */
    private int archiveStatus;

    /**
     * 项目版本 ID
     */
    private String projectVersionId;


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getInitialId() {
        return initialId;
    }

    public void setInitialId(String initialId) {
        this.initialId = initialId;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(int archiveStatus) {
        this.archiveStatus = archiveStatus;
    }

    public String getProjectVersionId() {
        return projectVersionId;
    }

    public void setProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
    }

}
