package com.mengtu.kaichi.serviceimpl.linkshow.service.impl;

import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectRepoService;
import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectVersionRepoService;
import com.mengtu.kaichi.linkshow.manager.LinkshowProjectManager;
import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.manager.OrganizationManager;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.serviceimpl.linkshow.request.ProjectRequest;
import com.mengtu.kaichi.serviceimpl.linkshow.service.ProjectService;
import com.mengtu.kaichi.user.idfactory.IdTypeEnum;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目管理服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:17
 */
@Service(value = "linkshowProjectServiceImpl")
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private LinkshowProjectManager linkshowProjectManager;

    @Resource
    private LinkshowProjectRepoService linkshowProjectRepoService;

    @Resource
    private LinkshowProjectVersionRepoService linkshowProjectVersionRepoService;

    @Resource
    private UserBasicService userBasicService;

    @Resource
    private OrganizationRepoService organizationRepoService;

    @Resource
    private OrganizationManager organizationManager;

    @Resource
    private ObsUtil obsUtil;

    /**
     * 头像 OBS 地址
     */
    private static final String AVATAR_LOCATION = "user/avatar/";

    /**
     * linkshow 项目 OBS 地址
     */
    private static final String PROJECT_LOCATION = "project/linkshow/";

    private static final String PROJECT_PREVIEW_LOCATION = "project/linkshow/preview/";

    @Override
    public ProjectBO create(ProjectRequest request) {
        verifyPerm(request);

        request.setInitialId(request.getUserId());
        return linkshowProjectManager.createProject(request);
    }

    @Override
    public ProjectBO update(ProjectRequest request) {
        return linkshowProjectManager.updateProject(request);
    }

    @Override
    public ProjectBO query(ProjectRequest request) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(request.getProjectId());
        projectBO = linkshowProjectRepoService.queryProject(projectBO);

        List<ProjectVersionBO> projectVersionBOList = linkshowProjectVersionRepoService.queryByProjectId(projectBO.getProjectId());

        if (!projectVersionBOList.isEmpty()) {
            ProjectVersionBO projectVersionBO = projectVersionBOList.get(projectVersionBOList.size() - 1);
            projectBO.putExtInfo("resource_uri", projectVersionBO.getResourceUri());
            projectBO.putExtInfo("data", obsUtil.downloadString(obsUtil.fetchFile(
                    PROJECT_LOCATION, projectVersionBO.getResourceUri())));
            if (projectBO.getModifyDate().before(projectVersionBO.getVersionTimestamp())) {
                projectBO.setModifyDate(projectVersionBO.getVersionTimestamp());
            }
            try {
                projectBO.putExtInfo("previewUrl", obsUtil.getSignatureDownloadUrl(
                        PROJECT_PREVIEW_LOCATION, projectVersionBO.getResourceUri()));
            } catch (Exception ignored) {
            }
        }

        return projectBO;
    }

    @Override
    public List<ProjectBO> queryAll(ProjectRequest request) {
        verifyPerm(request);

        ProjectBO projectBO = new ProjectBO();
        projectBO.setDomainId(request.getDomainId());

        List<ProjectBO> projectBOList = linkshowProjectRepoService.queryAllProject(projectBO);
        for (ProjectBO tempProjectBO : projectBOList) {
            List<ProjectVersionBO> projectVersionBOList = linkshowProjectVersionRepoService.queryByProjectId(tempProjectBO.getProjectId());
            if (!projectVersionBOList.isEmpty()) {
                ProjectVersionBO projectVersionBO = projectVersionBOList.get(projectVersionBOList.size() - 1);
                if (tempProjectBO.getModifyDate().before(projectVersionBO.getVersionTimestamp())) {
                    tempProjectBO.setModifyDate(projectVersionBO.getVersionTimestamp());
                }
                try {
                    try {
                        tempProjectBO.putExtInfo("previewUrl", obsUtil.getSignatureDownloadUrl(PROJECT_PREVIEW_LOCATION,
                                HashUtil.sha256(projectVersionBO.getProjectVersionId()), 300L));
                    } catch (Exception e) {
                        tempProjectBO.putExtInfo("previewUrl", obsUtil.getSignatureDownloadUrl(PROJECT_PREVIEW_LOCATION,
                                "fallback", 300L));
                    }
                } catch (Exception ignored) {
                }
            }

        }

        return projectBOList;
    }

    private void verifyPerm(ProjectRequest request) {
        String domainType = request.getDomainId().substring(20, 24);
        if (domainType.equals(IdTypeEnum.USER_ID.getBizId())) {
            AssertUtil.assertEquals(request.getDomainId(), request.getUserId(), "无权操作当前域 ID (用户)");
            AssertUtil.assertNotNull(userBasicService.getByUserId(request.getDomainId()), RestResultCode.NOT_FOUND, "指定的域 ID (用户)不存在");
        } else if (domainType.equals(com.mengtu.kaichi.organization.idfactory.IdTypeEnum.ORGANIZATION_ID.getBizNum())) {
            OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getDomainId());
            AssertUtil.assertNotNull(organizationBO, RestResultCode.NOT_FOUND, "指定的域 ID (组织)不存在");
            List<OrganizationBO> organizationBOList = CollectionUtil.toStream(organizationManager.queryOrganizationByMemberId(request.getUserId()))
                    .filter(Objects::nonNull)
                    .filter(temp -> (temp.getOrganizationId().equals(organizationBO.getOrganizationId())))
                    .collect(Collectors.toList());
            AssertUtil.assertTrue(!organizationBOList.isEmpty(), RestResultCode.FORBIDDEN, "无权操作当前域 ID (组织)");
        } else {
            throw new KaiChiException(RestResultCode.NOT_FOUND, "指定的域 ID 无效");
        }
    }

    @Override
    public List<ProjectBO> attachCreatorInfo(List<ProjectBO> projectBOList) {
        for (ProjectBO projectBO : projectBOList) {
            String initialUser = projectBO.getInitialId();
            UserInfoBO userInfoBO = userBasicService.getByUserId(initialUser).getUserInfo();
            projectBO.putExtInfo("initialUserName", userInfoBO.getNickname() != null ? userInfoBO.getNickname() : userInfoBO.getMobilePhone());
            projectBO.putExtInfo("initialAvatarUrl", obsUtil.getSignatureDownloadUrl(AVATAR_LOCATION, HashUtil.sha256(userInfoBO.getUserId()), 120L));
        }
        return projectBOList;
    }

    @Override
    public void archive(ProjectRequest request) {
        ProjectBO projectBO = new ProjectBO();
        projectBO.setProjectId(request.getProjectId());
        projectBO = linkshowProjectRepoService.queryProject(projectBO);
        AssertUtil.assertNotNull(projectBO, "项目 ID 不存在");

        request.setDomainId(projectBO.getDomainId());
        verifyPerm(request);

        linkshowProjectManager.archive(request);
    }

}
