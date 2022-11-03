package com.mengtu.kaichi.controller.linkpoint;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkpoint.LinkpointProjectVOConverter;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.model.linkpoint.request.ProjectRestRequest;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVO;
import com.mengtu.kaichi.position.pojo.LinkPointFolder;
import com.mengtu.kaichi.serviceimpl.linkpoint.builder.ProjectRequestBuilder;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.ProjectService;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.ProjectVersionService;
import com.mengtu.kaichi.serviceimpl.position.service.JobCategoryService;
import com.mengtu.kaichi.serviceimpl.position.service.LinkPointFolderService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * linkpoint 项目接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:27
 */
@CrossOrigin
@Controller(value = "linkpointProjectController")
@ResponseBody
@RequestMapping(value = "/linkpoint", produces = {"application/json;charset=UTF-8"})
public class ProjectController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);

    @Resource
    private ProjectService projectService;

    @Resource
    private ProjectVersionService projectVersionService;

    @Resource
    private LinkPointFolderService linkPointFolderService;

    /**
     * 创建 LinkPoint 项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVO> createProject(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "创建 LinkPoint 项目", request, new RestOperateCallBack<ProjectVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectName(), RestResultCode.ILLEGAL_PARAMETERS, "项目名称不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectDescription(), RestResultCode.ILLEGAL_PARAMETERS, "项目描述不能为空");
//                AssertUtil.assertStringNotBlank(request.getDomainId(), RestResultCode.ILLEGAL_PARAMETERS, "域 ID 不能为空");
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<ProjectVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectName(request.getProjectName())
                        .withProjectDescription(request.getProjectDescription())
                        .withFolderId(request.getFolderId())
                        .withUserId("202204241143307751750001202224")
                        .withDomainId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVOConverter.convert(projectService.create(builder.build())), "创建项目成功");
            }
        });
    }

    /**
     * 更新 LinkPoint 项目信息
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVO> updateProject(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "更新 LinkPoint 项目信息", request, new RestOperateCallBack<ProjectVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<ProjectVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withProjectName(request.getProjectName())
                        .withProjectDescription(request.getProjectDescription())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVOConverter.convert(projectService.update(builder.build())), "更新项目信息成功");
            }
        });
    }

    /**
     * 获取 LinkShow 项目实体信息
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVO> getProjectInstance(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint 项目实体", request, new RestOperateCallBack<ProjectVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
            }

            @Override
            public Result<ProjectVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVOConverter.convert(projectService.query(builder.build())), "获取项目实体信息成功");
            }
        });
    }

    /**
     * 获取 LinkPoint 项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/list")
    @ValidateToken
    @Limit
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ProjectVO>> getProject(ProjectRestRequest request, HttpServletRequest httpServletRequest,
                                              @RequestParam(value = "sort", required = false) String sortFlag,
                                              @RequestParam(value = "archiveStatus", required = false) String archiveStatus) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint 项目", request, new RestOperateCallBack<List<ProjectVO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
//                AssertUtil.assertStringNotBlank(request.getDomainId(), RestResultCode.ILLEGAL_PARAMETERS, "域 ID 不能为空");
                AssertUtil.assertStringNotBlank(archiveStatus, RestResultCode.ILLEGAL_PARAMETERS, "归档状态不能为空");
                AssertUtil.assertTrue(Objects.equals(archiveStatus, "0") || Objects.equals(archiveStatus, "1"), RestResultCode.ILLEGAL_PARAMETERS);
            }

            @Override
            public Result<List<ProjectVO>> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withDomainId("202204241143307751750001202224")
                        .withUserId("202204241143307751750001202224");
                List<ProjectBO> list = projectService.attachCreatorInfo(projectService.queryAll(builder.build(),request.getFolderId()));
                if (request.getFolderId() == null){
                    List<LinkPointFolder> linkPointFolders = linkPointFolderService.queryAll();
                    for (LinkPointFolder o:linkPointFolders) {
                        ProjectBO projectBO = new ProjectBO();
                        projectBO.setFolder(true);
                        projectBO.setProjectId(o.getId());
                        projectBO.setFolderId(o.getId());
                        projectBO.setProjectName(o.getFolderName());
                        projectBO.setCreateDate(o.getCreateTime());
                        projectBO.setModifyDate(o.getUpdateTime());
                        projectBO.setArchiveStatus(0);
                        list.add(projectBO);
                    }
                }
                return RestResultUtil.buildSuccessResult(CollectionUtil.toStream(
                                list)
                        .filter(Objects::nonNull)
                        .filter(temp -> (temp.getArchiveStatus().toString().equals(archiveStatus)))
                        .sorted(Comparator.comparingLong(o -> sortFlag == null ? o.getCreateDate().getTime() : o.getModifyDate().getTime()))
                        .map(LinkpointProjectVOConverter::convert)
                        .collect(Collectors.toList()), "获取项目列表成功");
            }
        });
    }

    /**
     * 变更 linkpoint 项目归档状态
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PatchMapping(value = "/archive")
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> changeArchiveStatus(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "变更 linkpoint 项目归档状态", request, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
            }

            @Override
            public Result<String> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withUserId("202204241143307751750001202224");
                projectService.archive(builder.build());

                return RestResultUtil.buildSuccessResult("归档状态变更成功");
            }
        });
    }

    /**
     * 复制 LinkPoint 项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/duplicate")
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVO> duplicateProject(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "复制 LinkPoint 项目", request, new RestOperateCallBack<ProjectVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
            }

            @Override
            public Result<ProjectVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVOConverter.convert(projectVersionService.duplicateVersion(builder.build())), "复制项目成功");
            }
        });
    }

}