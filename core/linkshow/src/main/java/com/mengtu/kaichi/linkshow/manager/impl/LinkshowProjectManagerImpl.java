package com.mengtu.kaichi.linkshow.manager.impl;

import com.mengtu.kaichi.linkshow.dal.repo.LinkshowProjectRepo;
import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectRepoService;
import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectVersionRepoService;
import com.mengtu.kaichi.linkshow.enums.ArchiveStatus;
import com.mengtu.kaichi.linkshow.enums.ProjectStatus;
import com.mengtu.kaichi.linkshow.manager.LinkshowProjectManager;
import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;
import com.mengtu.kaichi.linkshow.request.ProjectManageRequest;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.DateUtil;
import com.mengtu.util.tools.LoggerUtil;
import com.obs.services.model.ObsObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组织管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:47
 */
@Service
public class LinkshowProjectManagerImpl implements LinkshowProjectManager {

    private final Logger LOGGER = LoggerFactory.getLogger(LinkshowProjectManagerImpl.class);

    @Resource
    LinkshowProjectRepoService linkshowProjectRepoService;

    @Resource
    LinkshowProjectVersionRepoService linkshowProjectVersionRepoService;

    @Resource
    LinkshowProjectRepo linkshowProjectRepo;

    @Resource
    ObsUtil obsUtil;

    /**
     * 最大版本数
     */
    private static final int MAX_VERSION = 10;

    /**
     *
     */
    private static final String PROJECT_LOCATION = "project/linkshow/";

    private static final String PROJECT_PREVIEW_LOCATION = "project/linkshow/preview/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectBO createProject(ProjectManageRequest projectManageRequest) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectName(projectManageRequest.getProjectName());
        projectBO.setProjectDescription(projectManageRequest.getProjectDescription());
        projectBO.setDomainId(projectManageRequest.getDomainId());
        projectBO.setInitialId(projectManageRequest.getInitialId());
        projectBO.setArchiveStatus(ArchiveStatus.NORMAL.getDesc());
        projectBO.setStatus(ProjectStatus.UNDERWAY.getDesc());

        projectBO = linkshowProjectRepoService.createProject(projectBO);
        AssertUtil.assertNotNull(projectBO, RestResultCode.PARTIAL_CONTENT, "初始化创建项目失败");

        return projectBO;
    }

    @Override
    public ProjectBO updateProject(ProjectManageRequest projectManageRequest) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(projectManageRequest.getProjectId());
        projectBO.setProjectName(projectManageRequest.getProjectName());
        projectBO.setProjectDescription(projectManageRequest.getProjectDescription());
        projectBO.setDomainId(projectManageRequest.getDomainId());
        projectBO.setArchiveStatus(projectManageRequest.getArchiveStatus());
        projectBO.setStatus(projectManageRequest.getStatus());
        projectBO.setExtInfo(projectManageRequest.getExtInfo());

        return linkshowProjectRepoService.updateProject(projectBO);
    }

    @Override
    public void archive(ProjectManageRequest projectManageRequest) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(projectManageRequest.getProjectId());
        projectBO = linkshowProjectRepoService.queryProject(projectBO);

        AssertUtil.assertNotNull(projectBO, "项目信息不存在");
        int current = projectBO.getArchiveStatus();
        projectBO.setArchiveStatus(current == 0 ? 1 : 0);
        AssertUtil.assertNotEquals(current, linkshowProjectRepoService.updateProject(projectBO).getArchiveStatus(), "归档状态变更失败");
    }

    @Override
    public List<ProjectVersionBO> queryVersionList(ProjectManageRequest projectManageRequest) {
        List<ProjectVersionBO> projectVersionBOList = linkshowProjectVersionRepoService.queryByProjectId(projectManageRequest.getProjectId());
        AssertUtil.assertTrue(!projectVersionBOList.isEmpty(), RestResultCode.SYSTEM_ERROR, "项目版本信息异常");
        return projectVersionBOList;
    }

    @Override
    public ProjectBO duplicateVersion(ProjectManageRequest request) {

        ProjectVersionBO projectVersionBO = linkshowProjectVersionRepoService.queryByProjectVersionId(request.getProjectVersionId());
        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "项目版本信息不存在");

        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(projectVersionBO.getProjectId());
        projectBO = linkshowProjectRepoService.queryProject(projectBO);
        AssertUtil.assertNotNull(projectBO, RestResultCode.NOT_FOUND, "原始项目信息不存在");

        if (request.getProjectName() != null) {
            AssertUtil.assertNotEquals(projectBO.getProjectName(), request.getProjectName(), "新项目名称不能为与旧项目相同");
            projectBO.setProjectName(request.getProjectName());
        } else {
            projectBO.setProjectName(projectBO.getProjectName() + " -副本" + DateUtil.getShortDatesStr(new Date()));
        }
        projectBO.setProjectId(null);
        projectBO.setInitialId(request.getInitialId());
        projectBO = linkshowProjectRepoService.createProject(projectBO);
        AssertUtil.assertNotNull(projectBO, RestResultCode.PARTIAL_CONTENT, "初始化创建项目失败");


        ObsObject obsObject, preview = null;
        try {
            obsObject = obsUtil.fetchFile(PROJECT_LOCATION, projectVersionBO.getResourceUri());
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "获取原始项目文件失败");
        }
        AssertUtil.assertNotNull(obsObject, RestResultCode.NOT_FOUND, "获取原始项目文件失败");
        try {
            preview = obsUtil.fetchFile(PROJECT_PREVIEW_LOCATION, projectVersionBO.getResourceUri());
        } catch (Exception ignored) {
        }

        projectVersionBO.setProjectId(projectBO.getProjectId());

        projectVersionBO.setProjectVersionId(null);
        projectVersionBO.setResourceUri(null);
        projectVersionBO = linkshowProjectVersionRepoService.createProjectVersion(projectVersionBO);

        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "项目版本信息创建失败");

        try {
            String originalFileName = obsObject.getObjectKey();
            obsUtil.copy(obsObject, PROJECT_LOCATION
                    + projectVersionBO.getResourceUri()
                    + originalFileName.substring(originalFileName.lastIndexOf(".")));
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "复制原始项目文件失败");
        }
        if (preview != null) {
            String originalFileName = preview.getObjectKey();
            obsUtil.copy(preview, PROJECT_PREVIEW_LOCATION
                    + projectVersionBO.getResourceUri()
                    + originalFileName.substring(originalFileName.lastIndexOf(".")));
        }

        return projectBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVersionBO uploadData(ProjectManageRequest request, File file, File preview) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(request.getProjectId());
        projectBO = linkshowProjectRepoService.queryProject(projectBO);
        AssertUtil.assertNotNull(projectBO, "项目信息不存在");

        ProjectVersionBO projectVersionBO = new ProjectVersionBO();

        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(
                        linkshowProjectVersionRepoService.queryByProjectId(projectBO.getProjectId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                .collect(Collectors.toList());
        if (projectVersionBOList.size() >= MAX_VERSION) {
            for (int i = projectVersionBOList.size(); i >= MAX_VERSION; i--) {
                try {
                    obsUtil.deleteFile(PROJECT_LOCATION, projectVersionBOList.get(i - MAX_VERSION).getResourceUri());
                } catch (Exception e) {
                    LoggerUtil.warn(LOGGER, "OBS Delete Failed, location={0}, filename={1}", PROJECT_LOCATION, projectVersionBOList.get(i - 1).getResourceUri());
                }
                linkshowProjectVersionRepoService.deleteProjectVersion(projectVersionBOList.get(i - MAX_VERSION).getProjectVersionId());
            }
        }
        projectVersionBO.setProjectId(projectBO.getProjectId());
        projectVersionBO.setDomainId(projectBO.getDomainId());
        projectVersionBO.putExtInfo("operator_id", request.getInitialId());

        projectVersionBO = linkshowProjectVersionRepoService.createProjectVersion(projectVersionBO);
        AssertUtil.assertNotNull(projectVersionBO, "项目版本创建失败");


        String originalFileName = file.getName();
        obsUtil.uploadFile(file, PROJECT_LOCATION, projectVersionBO.getResourceUri() + originalFileName.substring(originalFileName.lastIndexOf(".")));
        if (preview != null) {
            originalFileName = preview.getName();
            obsUtil.uploadFile(preview, PROJECT_PREVIEW_LOCATION, projectVersionBO.getResourceUri() + originalFileName.substring(originalFileName.lastIndexOf(".")));
        }
        return projectVersionBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVersionBO checkoutVersion(ProjectManageRequest request) {
        ProjectVersionBO projectVersionBO = linkshowProjectVersionRepoService.queryByProjectVersionId(request.getProjectVersionId());
        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "检出的版本信息不存在");

        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(linkshowProjectVersionRepoService.queryByProjectId(projectVersionBO.getProjectId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                .collect(Collectors.toList());

        boolean flag = false;
        for (ProjectVersionBO versionBO : projectVersionBOList) {
            if (versionBO.getProjectVersionId().equals(projectVersionBO.getProjectVersionId())) {
                flag = true;
                projectVersionBO = versionBO;
                break;
            }
        }
        AssertUtil.assertTrue(flag, RestResultCode.SYSTEM_ERROR);

        int i, version = projectVersionBOList.indexOf(projectVersionBO);
        for (i = projectVersionBOList.size() - 1; i > version; i--) {
            try {
                linkshowProjectVersionRepoService.deleteProjectVersion(projectVersionBOList.get(i).getProjectVersionId());
                obsUtil.deleteFile(PROJECT_LOCATION, projectVersionBOList.get(i).getResourceUri());
                try {
                    obsUtil.deleteFile(PROJECT_PREVIEW_LOCATION, projectVersionBOList.get(i).getResourceUri());
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "版本检出异常");
            }
        }
        return projectVersionBOList.get(i);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllVersion(ProjectManageRequest request) {
        AssertUtil.assertStringNotBlank(request.getProjectId(), "项目 ID 不能为空");
        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(linkshowProjectVersionRepoService.queryByProjectId(request.getProjectId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                .collect(Collectors.toList());

        for (ProjectVersionBO projectVersionBO : projectVersionBOList) {
            try {
                linkshowProjectVersionRepoService.deleteProjectVersion(projectVersionBO.getProjectVersionId());
                obsUtil.deleteFile(PROJECT_LOCATION, projectVersionBO.getResourceUri());
                try {
                    obsUtil.deleteFile(PROJECT_PREVIEW_LOCATION, projectVersionBO.getResourceUri());
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "版本删除异常");
            }
        }
        try {
            linkshowProjectRepo.deleteByProjectId(request.getProjectId());
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "项目删除异常");
        }
    }

}