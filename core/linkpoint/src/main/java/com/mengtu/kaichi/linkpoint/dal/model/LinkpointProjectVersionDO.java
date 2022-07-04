package com.mengtu.kaichi.linkpoint.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * linkpoint 项目版本实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:31
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "linkpoint_project_version",
        indexes = {
                @Index(name = "uk_project_version_id", columnList = "project_version_id", unique = true),
                @Index(name = "nuk_project_id", columnList = "project_id")
        })
public class LinkpointProjectVersionDO extends BaseDO {

    private static final long serialVersionUID = -6258417101857412417L;

    /**
     * 项目 ID
     */
    @Column(name = "project_version_id", length = 32, nullable = false)
    private String projectVersionId;

    /**
     * 项目 ID
     */
    @Column(name = "project_id", length = 32, nullable = false)
    private String projectId;

    /**
     * 域 ID
     */
    @Column(name = "domain_id", length = 32, nullable = false)
    private String domainId;


    /**
     * 资源路径
     */
    @Column(name = "resource_uri")
    private String resourceUri;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getProjectVersionId() {
        return projectVersionId;
    }

    public void setProjectVersionId(String projectVersionId) {
        this.projectVersionId = projectVersionId;
    }

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

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}