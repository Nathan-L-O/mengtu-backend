package com.mengtu.kaichi.linkshow.dal.convert;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.linkshow.dal.model.LinkshowProjectDO;
import com.mengtu.kaichi.linkshow.dal.model.LinkshowProjectVersionDO;
import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;

import java.util.Map;

/**
 * 实体转换器
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 14:10
 */
final public class EntityConverter {

    /**
     * 项目 DO -> BO
     *
     * @param linkshowProjectDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ProjectBO convert(LinkshowProjectDO linkshowProjectDO) {
        if (linkshowProjectDO == null) {
            return null;
        }
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(linkshowProjectDO.getProjectId());
        projectBO.setProjectDescription(linkshowProjectDO.getProjectDescription());
        projectBO.setProjectName(linkshowProjectDO.getProjectName());
        projectBO.setArchiveStatus(linkshowProjectDO.getArchiveStatus());
        projectBO.setStatus(linkshowProjectDO.getStatus());
        projectBO.setInitialId(linkshowProjectDO.getInitialId());
        projectBO.setDomainId(linkshowProjectDO.getDomainId());
        projectBO.setCreateDate(linkshowProjectDO.getGmtCreate());
        projectBO.setModifyDate(linkshowProjectDO.getGmtModified());
        projectBO.setExtInfo(JSON.parseObject(linkshowProjectDO.getExtInfo(), Map.class));
        return projectBO;
    }

    /**
     * 项目 BO -> DO
     *
     * @param projectBO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkshowProjectDO convert(ProjectBO projectBO) {
        if (projectBO == null) {
            return null;
        }
        LinkshowProjectDO linkshowProjectDO = new LinkshowProjectDO();
        linkshowProjectDO.setProjectId(projectBO.getProjectId());
        linkshowProjectDO.setProjectDescription(projectBO.getProjectDescription());
        linkshowProjectDO.setProjectName(projectBO.getProjectName());
        linkshowProjectDO.setArchiveStatus(projectBO.getArchiveStatus());
        linkshowProjectDO.setStatus(projectBO.getStatus());
        linkshowProjectDO.setInitialId(projectBO.getInitialId());
        linkshowProjectDO.setDomainId(projectBO.getDomainId());
        linkshowProjectDO.setExtInfo(JSON.toJSONString((projectBO.getExtInfo())));
        return linkshowProjectDO;
    }

    /**
     * 项目版本 DO -> BO
     *
     * @param linkshowProjectVersionDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static ProjectVersionBO convert(LinkshowProjectVersionDO linkshowProjectVersionDO) {
        if (linkshowProjectVersionDO == null) {
            return null;
        }
        ProjectVersionBO projectVersionBO = new ProjectVersionBO();
        projectVersionBO.setProjectId(linkshowProjectVersionDO.getProjectId());
        projectVersionBO.setProjectVersionId(linkshowProjectVersionDO.getProjectVersionId());
        projectVersionBO.setDomainId(linkshowProjectVersionDO.getDomainId());
        projectVersionBO.setResourceUri(linkshowProjectVersionDO.getResourceUri());
        projectVersionBO.setVersionTimestamp(linkshowProjectVersionDO.getGmtModified());
        projectVersionBO.setExtInfo(JSON.parseObject(linkshowProjectVersionDO.getExtInfo(), Map.class));
        return projectVersionBO;
    }

    /**
     * 项目版本 BO -> DO
     *
     * @param projectVersionBO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static LinkshowProjectVersionDO convert(ProjectVersionBO projectVersionBO) {
        if (projectVersionBO == null) {
            return null;
        }
        LinkshowProjectVersionDO linkshowProjectVersionDO = new LinkshowProjectVersionDO();
        linkshowProjectVersionDO.setProjectId(projectVersionBO.getProjectId());
        linkshowProjectVersionDO.setProjectVersionId(projectVersionBO.getProjectVersionId());
        linkshowProjectVersionDO.setDomainId(projectVersionBO.getDomainId());
        linkshowProjectVersionDO.setResourceUri(projectVersionBO.getResourceUri());
        linkshowProjectVersionDO.setExtInfo(JSON.toJSONString((projectVersionBO.getExtInfo())));
        return linkshowProjectVersionDO;
    }

}