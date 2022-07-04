package com.mengtu.kaichi.controller.organization;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.organizaion.OrganizationInvitationVOConverter;
import com.mengtu.kaichi.model.organization.request.OrganizationRestRequest;
import com.mengtu.kaichi.model.organization.vo.OrganizationInvitationVO;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.serviceimpl.organization.builder.OrganizationRequestBuilder;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationInvitationService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 组织邀请接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/6 13:58
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/organization/invitation", produces = {"application/json;charset=UTF-8"})
public class OrganizationInvitationController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationInvitationController.class);

    @Resource
    private OrganizationInvitationService organizationInvitationService;

    /**
     * 创建邀请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
    @Limit(threshold = 30)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<OrganizationInvitationVO> createInvitation(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "创建组织邀请", request, new RestOperateCallBack<OrganizationInvitationVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
                AssertUtil.assertStringNotBlank(request.getLifetime(), RestResultCode.ILLEGAL_PARAMETERS, "生命周期不能为空");
            }

            @Override
            public Result<OrganizationInvitationVO> execute() {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(DateUtil.getDateByShortDatesStr(request.getLifetime()));
                calendar.add(Calendar.DATE, -90);
                if (new Date().after(calendar.getTime())) {
                    calendar.setTime(DateUtil.getDateByShortDatesStr(request.getLifetime()));
                } else {
                    calendar.setTime(new Date());
                    calendar.add(Calendar.DATE, 90);

                }

                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId())
                        .withLifetime(calendar.getTime());
                return RestResultUtil.buildSuccessResult(OrganizationInvitationVOConverter.convert(
                        organizationInvitationService.createInvitation(builder.build())), "创建邀请成功");
            }
        });
    }

    /**
     * 查询个人邀请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<OrganizationInvitationVO> queryInvitation(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询组织邀请", request, new RestOperateCallBack<OrganizationInvitationVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationId(), OrganizationErrorCode.INVALID_ORGANIZATION_ID);
            }

            @Override
            public Result<OrganizationInvitationVO> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationId(request.getOrganizationId());
                return RestResultUtil.buildSuccessResult(OrganizationInvitationVOConverter.convert(
                        organizationInvitationService.queryInvitation(builder.build())), "查询个人邀请成功");
            }
        });
    }

    /**
     * 查询邀请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/link")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<OrganizationInvitationVO> getInvitation(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "反查邀请", request, new RestOperateCallBack<OrganizationInvitationVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getEncodeUrl(), RestResultCode.ILLEGAL_PARAMETERS, "编码 URL 不能为空");
            }

            @Override
            public Result<OrganizationInvitationVO> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withEncodeUrl(request.getEncodeUrl());
                return RestResultUtil.buildSuccessResult(OrganizationInvitationVOConverter.convert(
                        organizationInvitationService.getInvitation(builder.build())), "反查邀请成功");
            }
        });
    }

    /**
     * 加入邀请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/link")
    @ValidateToken
    @Limit(threshold = 10)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> acceptInvitation(OrganizationRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "加入组织邀请", request, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getOrganizationInvitationId(), RestResultCode.ILLEGAL_PARAMETERS, "邀请 ID 不能为空");
            }

            @Override
            public Result<String> execute() {
                OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withOrganizationInvitationId(request.getOrganizationInvitationId());

                organizationInvitationService.acceptInvitation(builder.build());
                return RestResultUtil.buildSuccessResult("接收邀请成功");
            }
        });
    }

}
