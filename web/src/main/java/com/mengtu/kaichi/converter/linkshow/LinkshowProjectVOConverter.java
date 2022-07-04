package com.mengtu.kaichi.converter.linkshow;

import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.model.linkshow.vo.ProjectVO;

/**
 * 项目模型转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:36
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class LinkshowProjectVOConverter {

    private static final String USERNAME_INFO_KEY = "initialUserName";
    private static final String AVATAR_INFO_KEY = "initialAvatarUrl";

    private static final String PREVIEW_KEY = "previewUrl";

    public static ProjectVO convert(ProjectBO projectBO) {
        if (projectBO == null) {
            return null;
        }
        ProjectVO projectVO = new ProjectVO();
        projectVO.setProjectId(projectBO.getProjectId());
        if (projectBO.fetchExtInfo("data") != null) {
            projectVO.setSignatureUrl(projectBO.fetchExtInfo("data"));
        }
        projectVO.setProjectDescription(projectBO.getProjectDescription());
        projectVO.setProjectName(projectBO.getProjectName());
        projectVO.setArchiveStatus(projectBO.getArchiveStatus());
        projectVO.setDomainId(projectBO.getDomainId());
        projectVO.setInitialId(projectBO.getInitialId());
        projectVO.setCreateDate(projectBO.getCreateDate());
        projectVO.setModifyDate(projectBO.getModifyDate());
        projectVO.setStatus(projectBO.getStatus());
        if (projectBO.fetchExtInfo(USERNAME_INFO_KEY) != null) {
            projectVO.putExtInfo(USERNAME_INFO_KEY, projectBO.fetchExtInfo(USERNAME_INFO_KEY));
        }

        if (projectBO.fetchExtInfo(AVATAR_INFO_KEY) != null) {
            projectVO.putExtInfo(AVATAR_INFO_KEY, projectBO.fetchExtInfo(AVATAR_INFO_KEY));
        }
        projectVO.putExtInfo(PREVIEW_KEY, projectBO.fetchExtInfo(PREVIEW_KEY));
        return projectVO;
    }

}
