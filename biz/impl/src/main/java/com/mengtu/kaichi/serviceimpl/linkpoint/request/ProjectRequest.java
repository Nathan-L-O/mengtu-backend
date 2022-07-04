package com.mengtu.kaichi.serviceimpl.linkpoint.request;

import com.mengtu.kaichi.linkpoint.request.ProjectManageRequest;
import com.mengtu.kaichi.serviceimpl.common.verify.VerifyRequest;

/**
 * linkpoint 项目请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:34
 */
public class ProjectRequest extends ProjectManageRequest implements VerifyRequest {

    private static final long serialVersionUID = -1888920305515258266L;

    /**
     * 请求 ID
     */
    private String requestId;

    /**
     * 操作员 ID
     */
    private String userId;

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

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getDomainId() {
        return domainId;
    }

    @Override
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }

    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getProjectDescription() {
        return projectDescription;
    }

    @Override
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    @Override
    public String getProjectVersionId() {
        return projectVersionId;
    }

    @Override
    public void setProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
    }

    @Override
    public String getVerifyUserId() {
        return userId;
    }
}