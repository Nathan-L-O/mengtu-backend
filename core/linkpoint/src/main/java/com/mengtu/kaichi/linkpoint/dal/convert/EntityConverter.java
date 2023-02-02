package com.mengtu.kaichi.linkpoint.dal.convert;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.linkpoint.dal.model.LinkpointProjectDO;
import com.mengtu.kaichi.linkpoint.dal.model.LinkpointProjectVersionDO;
import com.mengtu.kaichi.linkpoint.dal.model.LinkpointVaultDO;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;

import java.util.Map;

/**
 * 实体转换器
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:40
 */
final public class EntityConverter {

    /**
     * 项目 DO -> BO
     *
     * @param linkpointProjectDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ProjectBO convert(LinkpointProjectDO linkpointProjectDO) {
        if (linkpointProjectDO == null) {
            return null;
        }
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(linkpointProjectDO.getProjectId());
        projectBO.setProjectDescription(linkpointProjectDO.getProjectDescription());
        projectBO.setProjectName(linkpointProjectDO.getProjectName());
        projectBO.setArchiveStatus(linkpointProjectDO.getArchiveStatus());
        projectBO.setStatus(linkpointProjectDO.getStatus());
        projectBO.setInitialId(linkpointProjectDO.getInitialId());
        projectBO.setDomainId(linkpointProjectDO.getDomainId());
        projectBO.setCreateDate(linkpointProjectDO.getGmtCreate());
        projectBO.setModifyDate(linkpointProjectDO.getGmtModified());
        projectBO.setPrincipalProject(linkpointProjectDO.getPrincipalProject());
        projectBO.setFolderId(linkpointProjectDO.getFolderId());
        projectBO.setExtInfo(JSON.parseObject(linkpointProjectDO.getExtInfo(), Map.class));
        return projectBO;
    }

    /**
     * 项目 BO -> DO
     *
     * @param projectBO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkpointProjectDO convert(ProjectBO projectBO) {
        if (projectBO == null) {
            return null;
        }
        LinkpointProjectDO linkpointProjectDO = new LinkpointProjectDO();
        linkpointProjectDO.setProjectId(projectBO.getProjectId());
        linkpointProjectDO.setProjectDescription(projectBO.getProjectDescription());
        linkpointProjectDO.setProjectName(projectBO.getProjectName());
        linkpointProjectDO.setArchiveStatus(projectBO.getArchiveStatus());
        linkpointProjectDO.setStatus(projectBO.getStatus());
        linkpointProjectDO.setInitialId(projectBO.getInitialId());
        linkpointProjectDO.setDomainId(projectBO.getDomainId());
        linkpointProjectDO.setExtInfo(JSON.toJSONString((projectBO.getExtInfo())));
        linkpointProjectDO.setFolderId(projectBO.getFolderId());
        linkpointProjectDO.setPrincipalProject(projectBO.getPrincipalProject());
        return linkpointProjectDO;
    }

    /**
     * 项目版本 DO -> BO
     *
     * @param linkpointProjectVersionDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ProjectVersionBO convert(LinkpointProjectVersionDO linkpointProjectVersionDO) {
        if (linkpointProjectVersionDO == null) {
            return null;
        }
        ProjectVersionBO projectVersionBO = new ProjectVersionBO();
        projectVersionBO.setProjectId(linkpointProjectVersionDO.getProjectId());
        projectVersionBO.setProjectVersionId(linkpointProjectVersionDO.getProjectVersionId());
        projectVersionBO.setDomainId(linkpointProjectVersionDO.getDomainId());
        projectVersionBO.setResourceUri(linkpointProjectVersionDO.getResourceUri());
        projectVersionBO.setVersionTimestamp(linkpointProjectVersionDO.getGmtModified());
        projectVersionBO.setDate(linkpointProjectVersionDO.getDate());
        projectVersionBO.setExtInfo(JSON.parseObject(linkpointProjectVersionDO.getExtInfo(), Map.class));
        return projectVersionBO;
    }

    /**
     * 项目版本 BO -> DO
     *
     * @param projectVersionBO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkpointProjectVersionDO convert(ProjectVersionBO projectVersionBO) {
        if (projectVersionBO == null) {
            return null;
        }
        LinkpointProjectVersionDO linkpointProjectVersionDO = new LinkpointProjectVersionDO();
        linkpointProjectVersionDO.setProjectId(projectVersionBO.getProjectId());
        linkpointProjectVersionDO.setProjectVersionId(projectVersionBO.getProjectVersionId());
        linkpointProjectVersionDO.setDomainId(projectVersionBO.getDomainId());
        linkpointProjectVersionDO.setResourceUri(projectVersionBO.getResourceUri());
        linkpointProjectVersionDO.setExtInfo(JSON.toJSONString((projectVersionBO.getExtInfo())));
        linkpointProjectVersionDO.setDate(projectVersionBO.getDate());
        return linkpointProjectVersionDO;
    }

    /**
     * 仓库模型 BO -> DO
     *
     * @param projectVaultBO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkpointVaultDO convert(ProjectVaultBO projectVaultBO) {
        if (projectVaultBO == null) {
            return null;
        }

        LinkpointVaultDO linkpointVaultDO = new LinkpointVaultDO();
        linkpointVaultDO.setModelId(projectVaultBO.getModelId());
        linkpointVaultDO.setModelName(projectVaultBO.getModelName());
        linkpointVaultDO.setStatus(projectVaultBO.getStatus());
        linkpointVaultDO.setHashtag(projectVaultBO.getHashtag());
        linkpointVaultDO.setExtInfo(JSON.toJSONString((projectVaultBO.getExtInfo())));
        return linkpointVaultDO;
    }

    /**
     * 仓库模型 DO -> BO
     *
     * @param linkpointVaultDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ProjectVaultBO convert(LinkpointVaultDO linkpointVaultDO) {
        if (linkpointVaultDO == null) {
            return null;
        }

        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setModelId(linkpointVaultDO.getModelId());
        projectVaultBO.setModelName(linkpointVaultDO.getModelName());
        projectVaultBO.setStatus(linkpointVaultDO.getStatus());
        projectVaultBO.setCreateDate(linkpointVaultDO.getGmtCreate());
        projectVaultBO.setModifyDate(linkpointVaultDO.getGmtModified());
        projectVaultBO.setHashtag(linkpointVaultDO.getHashtag());
        projectVaultBO.setExtInfo(JSON.parseObject(linkpointVaultDO.getExtInfo(), Map.class));
        return projectVaultBO;
    }

}