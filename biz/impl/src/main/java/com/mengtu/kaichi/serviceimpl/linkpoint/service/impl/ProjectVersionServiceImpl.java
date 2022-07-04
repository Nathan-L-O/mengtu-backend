package com.mengtu.kaichi.serviceimpl.linkpoint.service.impl;

import com.mengtu.kaichi.linkpoint.manager.LinkpointProjectManager;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;
import com.mengtu.kaichi.serviceimpl.linkpoint.request.ProjectRequest;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.ProjectService;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.ProjectVersionService;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目版本管理服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:17
 */
@Service(value = "linkpointProjectVersionServiceImpl")
public class ProjectVersionServiceImpl implements ProjectVersionService {

    @Resource
    private ProjectService projectService;

    @Resource
    private LinkpointProjectManager linkpointProjectManager;

    @Resource
    private UserBasicService userBasicService;

    @Override
    public List<ProjectVersionBO> queryVersionList(ProjectRequest request) {

        ProjectBO projectBO = projectService.query(request);
        AssertUtil.assertNotNull(projectBO, "项目 ID 不存在");
        AssertUtil.assertTrue(projectBO.getArchiveStatus() == 0, RestResultCode.FORBIDDEN, "项目已归档，拒绝访问");

        return linkpointProjectManager.queryVersionList(request);
    }

    @Override
    public List<ProjectVersionBO> attachOperatorName(List<ProjectVersionBO> projectVersionBOList) {
        for (ProjectVersionBO projectVersionBO : projectVersionBOList) {
            String initialUser = projectVersionBO.getExtInfo().get("operator_id");
            AssertUtil.assertStringNotBlank(initialUser, RestResultCode.ILLEGAL_PARAMETERS, "版本操作员未知");
            UserInfoBO userInfoBO = userBasicService.getByUserId(initialUser).getUserInfo();

            projectVersionBO.putExtInfo("operator_name", userInfoBO.getNickname() != null ? userInfoBO.getNickname() : userInfoBO.getMobilePhone());
        }
        return projectVersionBOList;
    }

    @Override
    public ProjectBO duplicateVersion(ProjectRequest request) {
        if (request.getProjectVersionId() == null) {
            AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "原始项目 ID 不能为空");
            List<ProjectVersionBO> projectVersionBOList;
            try {
                projectVersionBOList = CollectionUtil.toStream(linkpointProjectManager.queryVersionList(request))
                        .filter(Objects::nonNull)
                        .sorted(Comparator.comparingLong(o -> o.getVersionTimestamp().getTime()))
                        .collect(Collectors.toList());
            } catch (Exception e) {
                throw new KaiChiException(RestResultCode.PARTIAL_CONTENT, "原始项目暂无数据, 复制失败");
            }
            AssertUtil.assertTrue(!projectVersionBOList.isEmpty(), "原始项目版本信息异常");
            request.setProjectVersionId(projectVersionBOList.get(projectVersionBOList.size() - 1).getProjectVersionId());
        }
        request.setInitialId(request.getUserId());
        return linkpointProjectManager.duplicateVersion(request);
    }

    @Override
    public ProjectVersionBO versionCheckout(ProjectRequest request) {
        return linkpointProjectManager.checkoutVersion(request);
    }

    @Override
    public ProjectVersionBO uploadData(ProjectRequest request, File file, File preview) {
        request.setInitialId(request.getUserId());
        return linkpointProjectManager.uploadData(request, file, preview);
    }

}
