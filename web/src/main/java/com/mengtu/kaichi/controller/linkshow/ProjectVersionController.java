package com.mengtu.kaichi.controller.linkshow;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkshow.LinkshowProjectVOConverter;
import com.mengtu.kaichi.converter.linkshow.LinkshowProjectVersionVOConverter;
import com.mengtu.kaichi.model.linkshow.request.ProjectRestRequest;
import com.mengtu.kaichi.model.linkshow.vo.ProjectVO;
import com.mengtu.kaichi.model.linkshow.vo.ProjectVersionVO;
import com.mengtu.kaichi.serviceimpl.linkshow.builder.ProjectRequestBuilder;
import com.mengtu.kaichi.serviceimpl.linkshow.service.ProjectVersionService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * linkshow 项目接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:27
 */
@CrossOrigin
@Controller(value = "linkshowProjectVersionController")
@ResponseBody
@RequestMapping(value = "/linkshow/version", produces = {"application/json;charset=UTF-8"})
public class ProjectVersionController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(ProjectVersionController.class);
    @Resource
    private ProjectVersionService projectVersionService;

    /**
     * 获取 linkshow 项目版本列表
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
        return RestOperateTemplate.operate(LOGGER, "获取 linkshow 项目版本列表", request, new RestOperateCallBack<List<ProjectVersionVO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
            }

            @Override
            public Result<List<ProjectVersionVO>> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectId(request.getProjectId())
                        .withUserId(request.getUserId());

                return RestResultUtil.buildSuccessResult(CollectionUtil.toStream(
                                projectVersionService.attachOperatorName(projectVersionService.queryVersionList(builder.build())))
                        .filter(Objects::nonNull)
                        .map(LinkshowProjectVersionVOConverter::convert)
                        .collect(Collectors.toList()), "获取项目版本列表成功");
            }
        });
    }

    /**
     * 克隆 linkshow 历史版本项目
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
        return RestOperateTemplate.operate(LOGGER, "克隆 linkshow 历史版本项目", request, new RestOperateCallBack<ProjectVO>() {
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
                        .withUserId(request.getUserId());

                return RestResultUtil.buildSuccessResult(
                        LinkshowProjectVOConverter.convert(projectVersionService.duplicateVersion(builder.build())), "克隆历史版本成功");
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
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectId(), RestResultCode.ILLEGAL_PARAMETERS, "项目 ID 不能为空");
                AssertUtil.assertNotNull(file, RestResultCode.ILLEGAL_PARAMETERS, "上传数据不能为空");
                AssertUtil.assertNotNull(file, RestResultCode.ILLEGAL_PARAMETERS, "上传数据不能为空");
                AssertUtil.assertTrue(file.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "数据文件非法(空文件)");
                AssertUtil.assertTrue(file.getSize() <= 52428800L, RestResultCode.FORBIDDEN, "数据文件大小超限");
                if (preview != null) {
                    AssertUtil.assertTrue(preview.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "缩略图文件非法(空文件)");
                    AssertUtil.assertTrue(preview.getSize() <= 10485760L, RestResultCode.FORBIDDEN, "缩略图文件大小超限");
                }
            }

            @Override
            public Result<ProjectVersionVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withProjectId(request.getProjectId());

                return RestResultUtil.buildSuccessResult(
                        LinkshowProjectVersionVOConverter.convert(projectVersionService.uploadData(builder.build(),
                                FileUtil.multipartFileToFile(file), preview == null ? null : FileUtil.multipartFileToFile(preview))), "保存成功");
            }
        });
    }

    /**
     * linkshow 版本检出
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
        return RestOperateTemplate.operate(LOGGER, "linkshow 历史版本检出", request, new RestOperateCallBack<ProjectVersionVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getProjectVersionId(), RestResultCode.ILLEGAL_PARAMETERS, "项目版本 ID 不能为空");
            }

            @Override
            public Result<ProjectVersionVO> execute() {
                ProjectRequestBuilder builder = ProjectRequestBuilder.getInstance()
                        .withProjectVersionId(request.getProjectVersionId())
                        .withUserId(request.getUserId());

                return RestResultUtil.buildSuccessResult(
                        LinkshowProjectVersionVOConverter.convert(projectVersionService.versionCheckout(builder.build())), "版本检出成功");
            }
        });
    }

}