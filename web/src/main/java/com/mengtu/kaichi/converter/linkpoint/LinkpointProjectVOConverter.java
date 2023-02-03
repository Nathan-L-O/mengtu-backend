package com.mengtu.kaichi.converter.linkpoint;

import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVO;

/**
 * 项目模型转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:36
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class LinkpointProjectVOConverter {

    private static final String RESOURCE_URI = "resource_uri";
    private static final String USERNAME_INFO_KEY = "initialUserName";
    private static final String AVATAR_INFO_KEY = "initialAvatarUrl";

    private static final String PREVIEW_KEY = "previewUrl";
    private static final String PREVIEW_KEY_URI = "preview_uri";

    public static ProjectVO convert(ProjectBO projectBO) {
        if (projectBO == null) {
            return null;
        }
        ProjectVO projectVO = new ProjectVO();
        projectVO.setProjectId(projectBO.getProjectId());
//        if (projectBO.fetchExtInfo(RESOURCE_URI) != null) {
//            projectVO.setSignatureUrl(projectBO.fetchExtInfo(RESOURCE_URI));
//        }
        if (projectBO.getProjectId() != null) {
            projectVO.setSignatureUrl("http://192.168.3.8:8080/file?path=project/linkpoint/&key="+projectBO.getProjectId());
        }
        projectVO.setProjectDescription(projectBO.getProjectDescription());
        projectVO.setProjectName(projectBO.getProjectName());
        projectVO.setArchiveStatus(projectBO.getArchiveStatus());
        projectVO.setDomainId(projectBO.getDomainId());
        projectVO.setInitialId(projectBO.getInitialId());
        projectVO.setCreateDate(projectBO.getCreateDate());
        projectVO.setModifyDate(projectBO.getModifyDate());
        projectVO.setStatus(projectBO.getStatus());
        projectVO.setFolder(projectBO.getFolder());
        projectVO.setFolderId(projectBO.getFolderId());
        projectVO.setDate(projectBO.getDate());
        projectVO.setPrincipalProject(projectBO.getPrincipalProject());
        if (projectBO.fetchExtInfo(USERNAME_INFO_KEY) != null) {
            projectVO.putExtInfo(USERNAME_INFO_KEY, projectBO.fetchExtInfo(USERNAME_INFO_KEY));
        }

//        if (projectBO.fetchExtInfo(AVATAR_INFO_KEY) != null) {
//            projectVO.putExtInfo(AVATAR_INFO_KEY, projectBO.fetchExtInfo(AVATAR_INFO_KEY));
//        }
        projectVO.putExtInfo(PREVIEW_KEY, projectBO.fetchExtInfo(PREVIEW_KEY));
        projectVO.putExtInfo(PREVIEW_KEY_URI, projectBO.fetchExtInfo(PREVIEW_KEY_URI));
        return projectVO;
    }

}
