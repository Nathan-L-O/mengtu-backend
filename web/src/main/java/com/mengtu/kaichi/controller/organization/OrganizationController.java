package com.mengtu.kaichi.controller.organization;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.organizaion.OrganizationVOConverter;
import com.mengtu.kaichi.model.organization.request.OrganizationRestRequest;
import com.mengtu.kaichi.model.organization.vo.OrganizationVO;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.organization.builder.OrganizationRequestBuilder;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationManagerService;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.storage.FileUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组织接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/27 09:12
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/organization", produces = {"application/json;charset=UTF-8"})
public class OrganizationController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Resource
    private OrganizationRepoService organizationRepoService;

    @Resource
    private OrganizationManagerService organizationManagerService;

    @Resource
    private OrganizationService organizationService;

    @Value("${kaichi.user.avatar.max-size}")
    private int maxAvatarSize;


    /**
     * 获取组织
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<OrganizationVO>> getOrganization(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取组织", request, new RestOperateCallBack<List<OrganizationVO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
            }

            @Override
            public Result<List<OrganizationVO>> execute() {
                List<OrganizationVO> organizationVOList;

                if (StringUtils.isNotBlank(request.getOrganizationId())) {
                    organizationVOList = Collections.singletonList(OrganizationVOConverter.convert(organizationRepoService.queryByOrganizationId(request.getOrganizationId())));
                } else {
                    OrganizationRequest organizationRequest = new OrganizationRequest();
                    organizationRequest.setMemberId(request.getUserId());

                    List<String> currentUserOrganizationId = CollectionUtil.toStream(organizationService.queryOrganizationMemberByMemberId(organizationRequest))
                            .map(OrganizationMemberBO::getOrganizationId)
                            .distinct()
                            .collect(Collectors.toList());

                    organizationVOList = CollectionUtil.toStream(organizationRepoService.queryAllOrganization())
                            .filter(Objects::nonNull)
                            .filter(p -> currentUserOrganizationId.contains(p.getOrganizationId()))
                            .map(OrganizationVOConverter::convert)
                            .sorted(Comparator.comparingInt(o -> o.getFirstAlpha().charAt(0)))
                            .collect(Collectors.toList());

                }
                return RestResultUtil.buildSuccessResult(organizationVOList, "获取组织成功");
            }
        });
    }

    /**
     * 获取全部组织(暂不开放)
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/all")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<OrganizationVO>> getAllOrganization(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取组织列表", request, new RestOperateCallBack<List<OrganizationVO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
            }

            @Override
            public Result<List<OrganizationVO>> execute() {
                List<OrganizationVO> organizationVOList = CollectionUtil.toStream(organizationRepoService.queryAllOrganization())
                        .filter(Objects::nonNull)
                        .map(OrganizationVOConverter::convert)
                        .sorted(Comparator.comparingInt(o -> o.getFirstAlpha().charAt(0)))
                        .collect(Collectors.toList());
                return RestResultUtil.buildSuccessResult(organizationVOList, "获取组织列表成功");
            }
        });
    }

    /**
     * 创建组织
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<OrganizationVO> createOrganization(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "创建组织", request, new RestOperateCallBack<OrganizationVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationName(), OrganizationErrorCode.INVALID_ORGANIZATION_NAME);
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<OrganizationVO> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationName(request.getOrganizationName())
                        .withAttachUserId(request.getAttachUserId())
                        .withMemberDesc(request.getMemberDesc())
                        .withOrganizationType(request.getOrganizationType())
                        .withPrimaryOrganizationId(request.getPrimaryOrganizationId());
                return RestResultUtil.buildSuccessResult(OrganizationVOConverter.convert(organizationManagerService.create(builder.build())), "创建组织成功");
            }
        });
    }

    /**
     * 解散组织
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result disbandOrganization(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "解散组织", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
            }

            @Override
            public Result execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId());
                organizationManagerService.disband(builder.build());
                return RestResultUtil.buildSuccessResult(null, "解散组织成功");
            }
        });
    }

    /**
     * 组织头像登记
     *
     * @param file
     * @return
     */
    @PutMapping(value = "/avatar")
    @ValidateToken
    @Limit(threshold = 5)
    public Result<String> updateUserAvatar(OrganizationRestRequest request, HttpServletRequest httpServletRequest,
                                           @RequestParam(value = "avatar", required = false) MultipartFile file) {
        return RestOperateTemplate.operate(LOGGER, "更新头像", httpServletRequest, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertNotNull(file, RestResultCode.ILLEGAL_PARAMETERS, "上传头像不能为空");
                AssertUtil.assertTrue(file.getSize() != 0, "非法空文件");
                AssertUtil.assertTrue(file.getSize() <= maxAvatarSize, "头像文件大小超限");
            }

            @Override
            public Result<String> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId());

                organizationManagerService.avatarUpdate(builder.build(), FileUtil.multipartFileToFile(file));

                return RestResultUtil.buildSuccessResult("更新头像成功");
            }
        });
    }

    /**
     * 组织信息登记
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/info")
    @ValidateToken
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> updateOrganizationInfo(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "组织信息登记", request, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertStringNotBlank(request.getOrganizationName(), OrganizationErrorCode.INVALID_ORGANIZATION_NAME);
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<String> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId())
                        .withOrganizationName(request.getOrganizationName());
                organizationManagerService.updateInfo(builder.build());
                return RestResultUtil.buildSuccessResult("登记成功");
            }
        });
    }

}
