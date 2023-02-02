package com.mengtu.kaichi.controller.linkpoint;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkpoint.LinkpointProjectVOConverter;
import com.mengtu.kaichi.converter.linkpoint.LinkpointProjectVersionVOConverter;
import com.mengtu.kaichi.model.linkpoint.request.ProjectRestRequest;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVersionVO;
import com.mengtu.kaichi.serviceimpl.linkpoint.builder.ProjectRequestBuilder;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.ProjectVersionService;
import com.mengtu.kaichi.serviceimpl.position.service.LinkPointFolderService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.log.Log;
import com.mengtu.util.storage.FileUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * linkpoint 项目接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:27
 */
@CrossOrigin
@Controller(value = "linkpointProjectVersionController")
@ResponseBody
@RequestMapping(value = "/linkpoint/version", produces = {"application/json;charset=UTF-8"})
public class ProjectVersionController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectVersionController.class);
    @Resource
    private ProjectVersionService projectVersionService;
    @Resource
    private LinkPointFolderService linkPointFolderService;
    /**
     * 获取 LinkPoint 项目版本列表
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/list")
    @ValidateToken
    @Limit(threshold = 2)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<ProjectVersionVO>> getProject(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取 LinkPoint 项目版本列表", request, new RestOperateCallBack<List<ProjectVersionVO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
            }

            @Override
            public Result<List<ProjectVersionVO>> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(CollectionUtil.toStream(
                                projectVersionService.attachOperatorName(projectVersionService.queryVersionList(builder.build())))
                        .filter(Objects::nonNull)
                        .map(LinkpointProjectVersionVOConverter::convert)
                        .collect(Collectors.toList()), "获取项目版本列表成功");
            }
        });
    }

    /**
     * 克隆 LinkPoint 历史版本项目
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
        return RestOperateTemplate.operate(LOGGER, "克隆 LinkPoint 历史版本项目", request, new RestOperateCallBack<ProjectVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectVersionId(), RestResultCode.ILLEGAL_PARAMETERS, "项目版本 ID 不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectName(), RestResultCode.ILLEGAL_PARAMETERS, "新项目名称不能为空");
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<ProjectVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectVersionId(request.getProjectVersionId())
                        .withProjectName(request.getProjectName())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVOConverter.convert(projectVersionService.duplicateVersion(builder.build())), "克隆历史版本成功");
            }
        });
    }

    /**
     * 上传项目
     *
     * @param request
     * @param httpServletRequest
     * @param file
     * @return
     */
    @PostMapping
    @ValidateToken
    @Limit(threshold = 30)
    public Result<ProjectVersionVO> uploadProject(ProjectRestRequest request, HttpServletRequest httpServletRequest,
                                                  @RequestParam(value = "data", required = false) MultipartFile file,
                                                  @RequestParam(value = "preview", required = false) MultipartFile preview) {
        return RestOperateTemplate.operate(LOGGER, "上传项目", httpServletRequest, new RestOperateCallBack<ProjectVersionVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank("202204241143307751750001202224", RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
//                AssertUtil.assertStringNotBlank(md5Sum, RestResultCode.ILLEGAL_PARAMETERS, "数据摘要不能为空");
                AssertUtil.assertNotNull(file, RestResultCode.ILLEGAL_PARAMETERS, "上传数据不能为空");
                AssertUtil.assertTrue(file.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "数据文件非法(空文件)");
//                AssertUtil.assertTrue(file.getSize() <= 209715200L, RestResultCode.FORBIDDEN, "数据文件大小超限");
//                AssertUtil.assertTrue(md5Sum.equalsIgnoreCase(HashUtil.md5(file)), RestResultCode.FORBIDDEN, "数据完整性校验失败");
                if (preview != null) {
                    AssertUtil.assertTrue(preview.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "缩略图文件非法(空文件)");
//                    AssertUtil.assertTrue(preview.getSize() <= 10485760L, RestResultCode.FORBIDDEN, "缩略图文件大小超限");
                }
            }

            @Override
            public Result<ProjectVersionVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withUserId("202204241143307751750001202224")
                        .withProjectId(request.getProjectId())
                        .withDate(request.getDate());
                if (request.getFolderId()!=null){
                    linkPointFolderService.updateByFolderId(request.getFolderId());
                }
                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVersionVOConverter.convert(projectVersionService.uploadData(builder.build(),
                                FileUtil.multipartFileToFile(file), preview == null ? null : FileUtil.multipartFileToFile(preview))), "保存成功");
            }
        });
    }

    /**
     * LinkPoint 版本检出
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/checkout")
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVersionVO> versionCheckout(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "LinkPoint 历史版本检出", request, new RestOperateCallBack<ProjectVersionVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectVersionId(), RestResultCode.ILLEGAL_PARAMETERS, "项目版本 ID 不能为空");
            }

            @Override
            public Result<ProjectVersionVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectVersionId(request.getProjectVersionId())
                        .withUserId("202204241143307751750001202224");

                return RestResultUtil.buildSuccessResult(
                        LinkpointProjectVersionVOConverter.convert(projectVersionService.versionCheckout(builder.build())), "版本检出成功");
            }
        });
    }

}