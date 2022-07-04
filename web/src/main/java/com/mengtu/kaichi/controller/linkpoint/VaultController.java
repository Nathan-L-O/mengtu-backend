package com.mengtu.kaichi.controller.linkpoint;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkpoint.LinkPointProjectVaultListVOConverter;
import com.mengtu.kaichi.converter.linkpoint.LinkPointProjectVaultVOConverter;
import com.mengtu.kaichi.linkpoint.enums.ArchiveStatus;
import com.mengtu.kaichi.model.linkpoint.request.VaultRestRequest;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVaultListVO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVaultVO;
import com.mengtu.kaichi.serviceimpl.linkpoint.builder.VaultRequestBuilder;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.VaultService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.storage.FileUtil;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * linkpoint Vault 接口
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 14:02
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/linkpoint/vault", produces = {"application/json;charset=UTF-8"})
public class VaultController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(VaultController.class);

    public static final String SEPARATOR = "/";

    @Value("${kaichi.webapp.admin_token}")
    private String adminToken;

    @Resource
    private VaultService vaultService;

    /**
     * 创建 LinkPoint Vault 模型
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVaultVO> createModel(VaultRestRequest request, HttpServletRequest httpServletRequest,
                                              @RequestParam(value = "preview", required = false) MultipartFile preview,
                                              @RequestParam(value = "data", required = false) MultipartFile data) {
        return RestOperateTemplate.operate(LOGGER, "创建 LinkPoint Vault 模型", request, new RestOperateCallBack<ProjectVaultVO>() {
            @Override
            public void before() {
                AssertUtil.assertEquals(httpServletRequest.getHeader("Authorization"), adminToken, RestResultCode.FORBIDDEN);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                assertUri(request);
                AssertUtil.assertNotNull(data, RestResultCode.ILLEGAL_PARAMETERS, "上传数据不能为空");
                AssertUtil.assertTrue(data.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "数据文件非法(空文件)");
                AssertUtil.assertTrue(data.getSize() <= 209715200L, RestResultCode.FORBIDDEN, "数据文件大小超限");
                AssertUtil.assertNotNull(preview, RestResultCode.ILLEGAL_PARAMETERS, "上传缩略图不能为空");
                AssertUtil.assertTrue(preview.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "缩略图文件非法(空文件)");
                AssertUtil.assertTrue(preview.getSize() <= 10485760L, RestResultCode.FORBIDDEN, "缩略图文件大小超限");
                if (!StringUtils.isBlank(request.getHashtag())) {
                    assertTagString(request);
                } else {
                    request.setHashtag(null);
                }
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<ProjectVaultVO> execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withModelName(request.getUri().startsWith(SEPARATOR) ? request.getUri() : SEPARATOR + request.getUri())
                        .withSort(request.getSort())
                        .withHashtag(request.getHashtag())
                        .withUserId(request.getUserId());

                return RestResultUtil.buildSuccessResult(
                        LinkPointProjectVaultVOConverter.convert(vaultService.create(builder.build(),
                                FileUtil.multipartFileToFile(preview),
                                FileUtil.multipartFileToFile(data))), "创建 Vault 模型成功");
            }
        });
    }

    /**
     * 查询 LinkPoint Vault 列表
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/tree")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVaultListVO> queryList(VaultRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint Vault 列表", request, new RestOperateCallBack<ProjectVaultListVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
            }

            @Override
            public Result<ProjectVaultListVO> execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withStatus(ArchiveStatus.NORMAL.getDesc())
                        .withHashtag(StringUtils.isBlank(request.getHashtag()) ? null : request.getHashtag())
                        .withUserId(request.getUserId());


                return RestResultUtil.buildSuccessResult(
                        LinkPointProjectVaultListVOConverter.convert(vaultService.query(builder.build())), "查询 LinkPoint Vault 列表成功");
            }
        });
    }

    /**
     * 查询 LinkPoint Vault Tag 列表
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/tags")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<String>> queryTags(VaultRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint Vault Tag 列表", request, new RestOperateCallBack<List<String>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
            }

            @Override
            public Result<List<String>> execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withStatus(ArchiveStatus.NORMAL.getDesc())
                        .withUserId(request.getUserId());


                return RestResultUtil.buildSuccessResult(vaultService.getTagList(builder.build()), "查询 LinkPoint Vault Tag 列表成功");
            }
        });
    }

    /**
     * 查询 LinkPoint Vault 实体
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVaultVO> queryModel(VaultRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint Vault 实体", request, new RestOperateCallBack<ProjectVaultVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request.getModelId(), RestResultCode.ILLEGAL_PARAMETERS, "Vault 实体 Model ID 不能为空");
            }

            @Override
            public Result<ProjectVaultVO> execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withModelId(request.getModelId())
                        .withUserId(request.getUserId());


                return RestResultUtil.buildSuccessResult(
                        LinkPointProjectVaultVOConverter.convert(vaultService.get(builder.build())), "查询 LinkPoint Vault 实体成功");
            }
        });
    }

    /**
     * 修改 LinkPoint Vault 实体
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<ProjectVaultVO> modifyModel(VaultRestRequest request, HttpServletRequest httpServletRequest,
                                              @RequestParam(value = "preview", required = false) MultipartFile preview,
                                              @RequestParam(value = "data", required = false) MultipartFile data) {
        return RestOperateTemplate.operate(LOGGER, "修改 LinkPoint Vault 实体", request, new RestOperateCallBack<ProjectVaultVO>() {
            @Override
            public void before() {
                AssertUtil.assertEquals(httpServletRequest.getHeader("Authorization"), adminToken, RestResultCode.FORBIDDEN);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request.getModelId(), RestResultCode.ILLEGAL_PARAMETERS, "Vault 实体 Model ID 不能为空");
                if (!StringUtils.isBlank(request.getUri())) {
                    assertUri(request);
                    AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
                }
                if (data != null) {
                    AssertUtil.assertTrue(data.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "数据文件非法(空文件)");
                    AssertUtil.assertTrue(data.getSize() <= 209715200L, RestResultCode.FORBIDDEN, "数据文件大小超限");
                }
                if (preview != null) {
                    AssertUtil.assertTrue(preview.getSize() != 0, RestResultCode.ILLEGAL_PARAMETERS, "缩略图文件非法(空文件)");
                    AssertUtil.assertTrue(preview.getSize() <= 10485760L, RestResultCode.FORBIDDEN, "缩略图文件大小超限");
                }
                if (!StringUtils.isBlank(request.getHashtag())) {
                    assertTagString(request);
                    AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
                } else {
                    request.setHashtag(null);
                }
            }

            @Override
            public Result<ProjectVaultVO> execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withModelId(request.getModelId())
                        .withHashtag(request.getHashtag())
                        .withUserId(request.getUserId());
                if (request.getUri() != null) {
                    builder.withModelName(request.getUri().startsWith(SEPARATOR) ? request.getUri() : SEPARATOR + request.getUri());
                }
                if (request.getSort() != null) {
                    builder.withSort(request.getSort());
                }

                return RestResultUtil.buildSuccessResult(
                        LinkPointProjectVaultVOConverter.convert(vaultService.modify(builder.build(),
                                preview != null ? FileUtil.multipartFileToFile(preview) : null,
                                data != null ? FileUtil.multipartFileToFile(data) : null)), "修改 LinkPoint Vault 实体成功");
            }
        });
    }

    /**
     * 删除 LinkPoint Vault 实体
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result deleteModel(VaultRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询 LinkPoint Vault 实体", request, new RestOperateCallBack() {
            @Override
            public void before() {
                AssertUtil.assertEquals(httpServletRequest.getHeader("Authorization"), adminToken, RestResultCode.FORBIDDEN);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request.getModelId(), RestResultCode.ILLEGAL_PARAMETERS, "Vault 实体 Model ID 不能为空");
            }

            @Override
            public Result execute() {
                VaultRequestBuilder builder = VaultRequestBuilder.getInstance()
                        .withModelId(request.getModelId())
                        .withUserId(request.getUserId());
                vaultService.delete(builder.build());

                return RestResultUtil.buildSuccessResult("删除 LinkPoint Vault 实体成功");
            }
        });
    }

    private void assertUri(VaultRestRequest request) {
        AssertUtil.assertStringNotBlank(request.getUri(), RestResultCode.ILLEGAL_PARAMETERS, "资源标识符不能为空");
        AssertUtil.assertTrue(!request.getUri().endsWith(SEPARATOR), RestResultCode.ILLEGAL_PARAMETERS, "非法资源标识符: 纯路径");
        AssertUtil.assertTrue(request.getUri().lastIndexOf("//") == -1, RestResultCode.ILLEGAL_PARAMETERS, "非法资源标识符: 无效路径");
        AssertUtil.assertTrue(request.getUri().lastIndexOf("\\") == -1, RestResultCode.ILLEGAL_PARAMETERS, "非法资源标识符: 禁止转义字符");
        AssertUtil.assertTrue((LinkPointProjectVaultListVOConverter.getHierarchy(request.getUri().startsWith(SEPARATOR) ?
                request.getUri() : SEPARATOR + request.getUri())) == 2, RestResultCode.ILLEGAL_PARAMETERS, "非法资源标识符: 层级非法");
    }

    private void assertTagString(VaultRestRequest request) {
        AssertUtil.assertTrue(LinkPointProjectVaultVOConverter.getSize(request.getHashtag()) > 0, RestResultCode.ILLEGAL_PARAMETERS, "非法标签标识符: 层级非法");
    }

}