package com.mengtu.kaichi.serviceimpl.linkpoint.builder;

import com.mengtu.kaichi.serviceimpl.linkpoint.request.ProjectRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * linkpoint 项目请求构建器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:02
 */
final public class ProjectRequestBuilder {

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


    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public static ProjectRequestBuilder getInstance() {
        return new ProjectRequestBuilder();
    }

    public ProjectRequest build() {
        ProjectRequest request = new ProjectRequest();
        request.setRequestId(requestId);
        request.setProjectId(projectId);
        request.setProjectName(projectName);
        request.setProjectDescription(projectDescription);
        request.setProjectVersionId(projectVersionId);
        request.setDomainId(domainId);
        request.setUserId(userId);
        request.setExtInfo(extInfo);
        return request;
    }

    private ProjectRequestBuilder() {
    }

    public ProjectRequestBuilder withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public ProjectRequestBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public ProjectRequestBuilder withProjectId(String projectId) {
        this.projectId = projectId;
        return this;
    }

    public ProjectRequestBuilder withProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public ProjectRequestBuilder withProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public ProjectRequestBuilder withProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
        return this;
    }

    public ProjectRequestBuilder withDomainId(String domainId) {
        this.domainId = domainId;
        return this;
    }

    public ProjectRequestBuilder withExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
        return this;
    }

}