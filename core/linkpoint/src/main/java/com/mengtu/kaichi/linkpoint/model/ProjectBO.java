package com.mengtu.kaichi.linkpoint.model;

import com.mengtu.util.common.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * linkpoint 项目模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:43
 */
public class ProjectBO extends ToString {

    private static final long serialVersionUID = -1297082831114240855L;

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
    private Integer status;

    /**
     * 归档状态
     */
    private Integer archiveStatus;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 修改时间
     */
    private Date modifyDate;

    /**
     * 所属文件夹ID
     */
    private String folderId;

    /**
     * 为父项目
     */
    private Boolean isPrincipalProject;

    /**
     * 判断是否为文件夹
     */
    private Boolean isFolder;


    /**
     * 拓展信息
     */
    private Map<String, String> extInfo;

    /**
     * 放入拓展信息
     *
     * @param key
     * @param value
     */
    public void putExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }

    /**
     * 取出拓展信息
     *
     * @param key
     * @return
     * @see
     */
    public String fetchExtInfo(String key) {
        if (extInfo == null) {
            return null;
        }
        return extInfo.get(key);
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public Boolean getPrincipalProject() {
        return isPrincipalProject;
    }

    public void setPrincipalProject(Boolean principalProject) {
        isPrincipalProject = principalProject;
    }

    public Boolean getFolder() {
        return isFolder;
    }

    public void setFolder(Boolean folder) {
        isFolder = folder;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}