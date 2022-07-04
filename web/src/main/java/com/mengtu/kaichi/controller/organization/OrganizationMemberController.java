package com.mengtu.kaichi.controller.organization;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.model.organization.request.OrganizationRestRequest;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.organization.builder.OrganizationRequestBuilder;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationManagerService;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.tools.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 组织成员接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/27 09:13
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/organization/member", produces = {"application/json;charset=UTF-8"})
public class OrganizationMemberController {

    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationMemberController.class);

    @Resource
    private OrganizationManagerService organizationManagerService;

    @Resource
    private OrganizationService organizationService;

    /**
     * 添加成员
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ValidateToken
    @PostMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result addMember(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "添加成员", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertStringNotBlank(request.getAttachUserId(), RestResultCode.ILLEGAL_PARAMETERS, "成员ID不能为空");
                AssertUtil.assertStringNotBlank(request.getMemberType(), RestResultCode.ILLEGAL_PARAMETERS, "成员类型不能为空");
            }

            @Override
            public Result execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId())
                        .withAttachUserId(request.getAttachUserId())
                        .withMemberType(request.getMemberType())
                        .withMemberDesc(request.getMemberDesc());
                organizationManagerService.addMember(builder.build());
                return RestResultUtil.buildSuccessResult(null, "添加成员成功");
            }
        });
    }

    /**
     * 修改成员身份
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ValidateToken
    @PutMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result manageMemberType(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "修改成员身份", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertStringNotBlank(request.getAttachUserId(), RestResultCode.ILLEGAL_PARAMETERS, "成员Id不能为空");
                AssertUtil.assertStringNotBlank(request.getMemberType(), RestResultCode.ILLEGAL_PARAMETERS, "成员类型");
            }

            @Override
            public Result execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId())
                        .withAttachUserId(request.getAttachUserId())
                        .withMemberType(request.getMemberType())
                        .withMemberDesc(request.getMemberDesc());
                organizationManagerService.changeMemberType(builder.build());
                return RestResultUtil.buildSuccessResult(null, "修改成员身份成功");
            }
        });
    }

    /**
     * 移出成员
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ValidateToken
    @DeleteMapping
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result removeMember(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "移除成员", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertStringNotBlank(request.getAttachUserId(), RestResultCode.ILLEGAL_PARAMETERS, "成员Id不能为空");
            }

            @Override
            public Result execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId())
                        .withAttachUserId(request.getAttachUserId());
                organizationManagerService.removeMember(builder.build());
                return RestResultUtil.buildSuccessResult(null, "移除成员成功");
            }
        });
    }

    @ValidateToken
    @GetMapping
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<List<OrganizationMemberBO>>> queryMember(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询成员", request, new RestOperateCallBack<List<List<OrganizationMemberBO>>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
            }

            @Override
            public Result<List<List<OrganizationMemberBO>>> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId());

                return RestResultUtil.buildSuccessResult(
                        organizationService.queryOrganizationMemberByOrganizationId(builder.build()), "查询成员成功"
                );
            }
        });
    }

}