package com.mengtu.kaichi.linkpoint.manager.impl;

import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointProjectRepo;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectRepoService;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectVersionRepoService;
import com.mengtu.kaichi.linkpoint.enums.ArchiveStatus;
import com.mengtu.kaichi.linkpoint.enums.ProjectStatus;
import com.mengtu.kaichi.linkpoint.manager.LinkpointProjectManager;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;
import com.mengtu.kaichi.linkpoint.request.ProjectManageRequest;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.storage.FileUtil;
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
 * 项目管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:47
 */
@Service
public class LinkpointProjectManagerImpl implements LinkpointProjectManager {

    private final Logger LOGGER = LoggerFactory.getLogger(LinkpointProjectManagerImpl.class);

    @Resource
    LinkpointProjectRepoService linkpointProjectRepoService;

    @Resource
    LinkpointProjectVersionRepoService linkpointProjectVersionRepoService;

    @Resource
    LinkpointProjectRepo linkpointProjectRepo;

    @Resource
    ObsUtil obsUtil;

    /**
     * 最大版本数
     */
    private static final int MAX_VERSION = 10;

    /**
     *
     */
    private static final String PROJECT_LOCATION = "project/linkpoint/";

    private static final String PROJECT_PREVIEW_LOCATION = "project/linkpoint/preview/";

    private static final String PREVIEW_LOCATION = "preview/linkpoint/";

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
        if (projectManageRequest.getFolderId() != null){
            projectBO.setFolderId(projectManageRequest.getFolderId());
//            如果为改文件夹中创建的第一个项目，设置为主项目
            if (linkpointProjectRepoService.queryByFolderId(projectManageRequest.getFolderId()).size()==0)
                projectBO.setPrincipalProject(true);
        }
        projectBO = linkpointProjectRepoService.createProject(projectBO);
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
        return linkpointProjectRepoService.updateProject(projectBO);
    }

    @Override
    public void archive(ProjectManageRequest projectManageRequest) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(projectManageRequest.getProjectId());
        projectBO = linkpointProjectRepoService.queryProject(projectBO);

        AssertUtil.assertNotNull(projectBO, "项目信息不存在");
        int current = projectBO.getArchiveStatus();
        projectBO.setArchiveStatus(current == 0 ? 1 : 0);
        AssertUtil.assertNotEquals(current, linkpointProjectRepoService.updateProject(projectBO).getArchiveStatus(), "归档状态变更失败");
    }

    @Override
    public List<ProjectVersionBO> queryVersionList(ProjectManageRequest projectManageRequest) {
        List<ProjectVersionBO> projectVersionBOList = linkpointProjectVersionRepoService.queryByProjectId(projectManageRequest.getProjectId());
        AssertUtil.assertTrue(!projectVersionBOList.isEmpty(), RestResultCode.SYSTEM_ERROR, "项目版本信息异常");
        return projectVersionBOList;
    }

    @Override
    public ProjectBO duplicateVersion(ProjectManageRequest request) {

        ProjectVersionBO projectVersionBO = linkpointProjectVersionRepoService.queryByProjectVersionId(request.getProjectVersionId());
        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "项目版本信息不存在");

        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(projectVersionBO.getProjectId());
        projectBO = linkpointProjectRepoService.queryProject(projectBO);
        AssertUtil.assertNotNull(projectBO, RestResultCode.NOT_FOUND, "原始项目信息不存在");

        if (request.getProjectName() != null) {
            AssertUtil.assertNotEquals(projectBO.getProjectName(), request.getProjectName(), "新项目名称不能为与旧项目相同");
            projectBO.setProjectName(request.getProjectName());
        } else {
            projectBO.setProjectName(projectBO.getProjectName() + " -副本" + DateUtil.getShortDatesStr(new Date()));
        }
        projectBO.setProjectId(null);
        projectBO.setInitialId(request.getInitialId());
        projectBO = linkpointProjectRepoService.createProject(projectBO);
        AssertUtil.assertNotNull(projectBO, RestResultCode.PARTIAL_CONTENT, "初始化创建项目失败");


        File obsObject, preview = null;
        try {
            obsObject = FileUtil.getFile(PROJECT_LOCATION, projectVersionBO.getResourceUri());
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "获取原始项目文件失败");
        }
        AssertUtil.assertNotNull(obsObject, RestResultCode.NOT_FOUND, "获取原始项目文件失败");
        try {
            preview = FileUtil.getFile(PREVIEW_LOCATION, projectVersionBO.getResourceUri());
        } catch (Exception ignored) {
        }


        projectVersionBO.setProjectId(projectBO.getProjectId());

        projectVersionBO.setProjectVersionId(null);
        projectVersionBO.setResourceUri(null);
        projectVersionBO = linkpointProjectVersionRepoService.createProjectVersion(projectVersionBO);

        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "项目版本信息创建失败");

        try {
            FileUtil.putFile(obsObject, PROJECT_LOCATION
                    , projectVersionBO.getResourceUri());
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "复制原始项目文件失败");
        }

        if (preview != null) {
            FileUtil.putFile(preview, PREVIEW_LOCATION
                    , projectVersionBO.getResourceUri());
        }

        return projectBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVersionBO uploadData(ProjectManageRequest request, File file, File preview) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(request.getProjectId());
        projectBO = linkpointProjectRepoService.queryProject(projectBO);
        AssertUtil.assertNotNull(projectBO, "项目信息不存在");

        ProjectVersionBO projectVersionBO = new ProjectVersionBO();

        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(
                        linkpointProjectVersionRepoService.queryByProjectId(projectBO.getProjectId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                .collect(Collectors.toList());

        if (projectVersionBOList.size() >= MAX_VERSION) {
            for (int i = projectVersionBOList.size(); i >= MAX_VERSION; i--) {
                try {
                    FileUtil.deleteFile(PROJECT_LOCATION, projectVersionBOList.get(i - MAX_VERSION).getResourceUri());
                    FileUtil.deleteFile(PREVIEW_LOCATION, projectVersionBOList.get(i - MAX_VERSION).getResourceUri());
                } catch (Exception e) {
                    LoggerUtil.warn(LOGGER, "OBS Delete Failed, location={0}, filename={1}", PROJECT_LOCATION, projectVersionBOList.get(i - 1).getResourceUri());
                }
                linkpointProjectVersionRepoService.deleteProjectVersion(projectVersionBOList.get(i - MAX_VERSION).getProjectVersionId());
            }
        }
        projectVersionBO.setProjectId(projectBO.getProjectId());
        projectVersionBO.setDomainId(projectBO.getDomainId());
        projectVersionBO.putExtInfo("operator_id", request.getInitialId());

        projectVersionBO = linkpointProjectVersionRepoService.createProjectVersion(projectVersionBO);
        AssertUtil.assertNotNull(projectVersionBO, "项目版本创建失败");


        String originalFileName = file.getName();
        FileUtil.putFile(file, PROJECT_LOCATION, projectVersionBO.getResourceUri() + originalFileName.substring(originalFileName.lastIndexOf(".")));
        if (preview != null) {
            originalFileName = preview.getName();
            FileUtil.putFile(preview, PREVIEW_LOCATION, projectVersionBO.getResourceUri() + originalFileName.substring(originalFileName.lastIndexOf(".")));
        }

        return projectVersionBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVersionBO checkoutVersion(ProjectManageRequest request) {
        ProjectVersionBO projectVersionBO = linkpointProjectVersionRepoService.queryByProjectVersionId(request.getProjectVersionId());
        AssertUtil.assertNotNull(projectVersionBO, RestResultCode.NOT_FOUND, "检出的版本信息不存在");

        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(linkpointProjectVersionRepoService.queryByProjectId(projectVersionBO.getProjectId()))
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
                linkpointProjectVersionRepoService.deleteProjectVersion(projectVersionBOList.get(i).getProjectVersionId());
                FileUtil.deleteFile(PROJECT_LOCATION, projectVersionBOList.get(i).getResourceUri());
                try {
                    FileUtil.deleteFile(PREVIEW_LOCATION, projectVersionBOList.get(i).getResourceUri());
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
        List<ProjectVersionBO> projectVersionBOList = CollectionUtil.toStream(linkpointProjectVersionRepoService.queryByProjectId(request.getProjectId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                .collect(Collectors.toList());

        for (ProjectVersionBO projectVersionBO : projectVersionBOList) {
            try {
                linkpointProjectVersionRepoService.deleteProjectVersion(projectVersionBO.getProjectVersionId());
                FileUtil.deleteFile(PROJECT_LOCATION, projectVersionBO.getResourceUri());
                try {
                    FileUtil.deleteFile(PREVIEW_LOCATION, projectVersionBO.getResourceUri());
                } catch (Exception ignored) {
                }
            } catch (Exception e) {
                throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "版本删除异常");
            }
        }
        try {
            linkpointProjectRepo.deleteByProjectId(request.getProjectId());
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "项目删除异常");
        }
    }
}