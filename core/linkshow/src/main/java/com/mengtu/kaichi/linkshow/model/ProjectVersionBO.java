package com.mengtu.kaichi.linkshow.model;

import com.mengtu.util.common.ToString;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * linkshow 项目历史模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:43
 */
public class ProjectVersionBO extends ToString {

    /**
     * 项目版本 ID
     */
    private String projectVersionId;

    /**
     * 项目 ID
     */
    private String projectId;

    /**
     * 域 ID
     */
    private String domainId;


    /**
     * 资源路径
     */
    private String resourceUri;

    /**
     * 时间戳
     */
    private Date versionTimestamp;


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

    public Date getVersionTimestamp() {
        return versionTimestamp;
    }

    public void setVersionTimestamp(Date versionTimestamp) {
        this.versionTimestamp = versionTimestamp;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}