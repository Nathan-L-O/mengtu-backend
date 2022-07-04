package com.mengtu.kaichi.linkshow.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * linkshow 项目实体
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/16 09:27
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "linkshow_project",
        indexes = {
                @Index(name = "uk_project_id", columnList = "project_id", unique = true),
                @Index(name = "nuk_domain_id", columnList = "domain_id")
        })
public class LinkshowProjectDO extends BaseDO {

    private static final long serialVersionUID = -5939009273515306444L;

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
     * 初始化 ID
     */
    @Column(name = "initial_id", length = 32, nullable = false)
    private String initialId;

    /**
     * 项目名称
     */
    @Column(name = "project_name", nullable = false)
    private String projectName;

    /**
     * 项目描述
     */
    @Column(name = "project_description")
    private String projectDescription;

    /**
     * 项目状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 归档状态
     */
    @Column(name = "archive_status")
    private Integer archiveStatus;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getArchiveStatus() {
        return archiveStatus;
    }

    public void setArchiveStatus(Integer archiveStatus) {
        this.archiveStatus = archiveStatus;
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