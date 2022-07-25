package com.mengtu.kaichi.controller.user;

import com.mengtu.kaichi.common.annotation.Captcha;
import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.user.UserVOConverter;
import com.mengtu.kaichi.model.user.request.UserInfoRequest;
import com.mengtu.kaichi.model.user.request.UserRequestBase;
import com.mengtu.kaichi.model.user.vo.UserVO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.common.constant.UserRequestExtInfoKey;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.serviceimpl.user.builder.CommonUserRequestBuilder;
import com.mengtu.kaichi.serviceimpl.user.request.CommonUserRequest;
import com.mengtu.kaichi.serviceimpl.user.service.UserService;
import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import com.mengtu.kaichi.user.enums.UserErrorCode;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.util.IPUtil;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.storage.FileUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.DateUtil;
import com.mengtu.util.verification.VerificationCodeUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户接口控制器
 *
 * @author 过昊天
 * @version 1.4 @ 2022/5/25 11:15
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/user", produces = {"application/json;charset=UTF-8"})
public class UserController {

    /**
     * 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private OrganizationService organizationService;

    @Resource
    VerificationCodeUtil verificationCodeUtil;

    @Value("${kaichi.user.avatar.max-size}")
    private int maxAvatarSize;

    /**
     * 登陆（获取 token）
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @Captcha
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserVO> login(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户登录", null, new RestOperateCallBack<UserVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertStringNotBlank(request.getPassword(), UserErrorCode.INVALID_PASSWORD);
            }

            @Override
            public Result<UserVO> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername())
                        .withPassword(request.getPassword());
                OperateContext operateContext = new OperateContext();
                operateContext.setOperateIp(IPUtil.getIp(httpServletRequest));
                UserVO userVO = UserVOConverter.convert(userService.injectAvatarUrl(
                        userService.login(commonUserRequestBuilder.build(), operateContext)
                ));

                OrganizationRequest organizationRequest = new OrganizationRequest();
                organizationRequest.setMemberId(userVO.getUserId());
                userVO.setJobInfo(CollectionUtil.toStream(organizationService.queryOrganizationMemberByMemberId(organizationRequest))
                        .map(OrganizationMemberBO::findJob)
                        .distinct()
                        .collect(Collectors.toList())
                );

                return RestResultUtil.buildSuccessResult(userVO, "登陆成功");
            }
        });
    }

    /**
     * 获取登陆短信验证码
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/sms")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Object> getSmsCode(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户请求登陆短信验证", null, new RestOperateCallBack<Object>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
            }

            @Override
            public Result<Object> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername());
                OperateContext operateContext = new OperateContext();
                operateContext.setOperateIp(IPUtil.getIp(httpServletRequest));

                try {
                    userService.smsLogin(commonUserRequestBuilder.build(), operateContext);
                } catch (Exception e) {
                    return RestResultUtil.buildFailResult(RestResultCode.NOT_FOUND, "用户尚未注册");
                }

                verificationCodeUtil.initLoginSmsCode(request.getUsername());
                return RestResultUtil.buildSuccessResult("验证码已下发");
            }
        });
    }

    /**
     * 短信验证码登陆
     *
     * @param request
     * @param httpServletRequest
     * @param smsCode
     * @return
     */
    @PostMapping(value = "/sms")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserVO> smsLogin(UserRequestBase request, HttpServletRequest httpServletRequest,
                                   @RequestParam(value = "smsCode", required = false) String smsCode) {
        return RestOperateTemplate.operate(LOGGER, "用户通过短信验证码登陆", null, new RestOperateCallBack<UserVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertStringNotBlank(smsCode, RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
                VerificationCodeUtil.validate(request.getUsername(), smsCode);
            }

            @Override
            public Result<UserVO> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername());
                OperateContext operateContext = new OperateContext();
                operateContext.setOperateIp(IPUtil.getIp(httpServletRequest));
                UserVO userVO = UserVOConverter.convert(userService.injectAvatarUrl(
                        userService.smsLogin(commonUserRequestBuilder.build(), operateContext)
                ));

                OrganizationRequest organizationRequest = new OrganizationRequest();
                organizationRequest.setMemberId(userVO.getUserId());
                userVO.setJobInfo(CollectionUtil.toStream(organizationService.queryOrganizationMemberByMemberId(organizationRequest))
                        .map(OrganizationMemberBO::findJob)
                        .distinct()
                        .collect(Collectors.toList())
                );

                return RestResultUtil.buildSuccessResult(userVO, "登陆成功");
            }
        });
    }

    /**
     * 登陆（维持）
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserVO> checkLogin(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户登录维持", request, new RestOperateCallBack<UserVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
            }

            @Override
            public Result<UserVO> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUserId(request.getUserId());
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                UserVO userVO = UserVOConverter.convert(userService.injectAvatarUrl(
                        userService.fetchUser(commonUserRequestBuilder.build(), context)
                ));

                OrganizationRequest organizationRequest = new OrganizationRequest();
                organizationRequest.setMemberId(userVO.getUserId());
                userVO.setJobInfo(CollectionUtil.toStream(organizationService.queryOrganizationMemberByMemberId(organizationRequest))
                        .map(OrganizationMemberBO::findJob)
                        .distinct()
                        .collect(Collectors.toList())
                );

                return RestResultUtil.buildSuccessResult(userVO, "登陆维持成功");
            }
        });
    }

    /**
     * 注销登陆
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> logout(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户注销登陆", request, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUserId(), UserErrorCode.INVALID_USERNAME);
            }

            @Override
            public Result<String> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUserId(request.getUserId());
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                userService.logout(commonUserRequestBuilder.build(), context);
                return RestResultUtil.buildSuccessResult("注销成功");
            }
        });
    }

    /**
     * 修改密码
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/password")
    @ValidateToken
    @Limit(threshold = 300)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> modifyPassword(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "修改密码", null, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getPassword(), RestResultCode.ILLEGAL_PARAMETERS, "原密码不能为空");
                AssertUtil.assertStringNotBlank(request.getNewPassword(), RestResultCode.ILLEGAL_PARAMETERS, "新密码不能为空");
                AssertUtil.assertTrue(!StringUtils.equals(request.getPassword(), request.getNewPassword()), "新旧密码不能一致");
            }

            @Override
            public Result<String> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withPassword(request.getPassword())
                        .withUserId(request.getUserId());
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));

                CommonUserRequest commonUserRequest = commonUserRequestBuilder.build();
                commonUserRequest.putExtInfo(UserRequestExtInfoKey.USER_NEW_PASSWORD, request.getNewPassword());

                userService.modifyUser(commonUserRequest, context);
                return RestResultUtil.buildSuccessResult("密码修改成功");
            }
        });
    }

    /**
     * 获取注册验证码
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/register")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Object> getRegCode(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户请求注册短信验证", null, new RestOperateCallBack<Object>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
            }

            @Override
            public Result<Object> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername())
                        .withPassword("COMMON_PASSWORD_FOR_VALIDATE");

                userService.registerValidate(commonUserRequestBuilder.build());

                verificationCodeUtil.initRegisterSmsCode(request.getUsername());
                return RestResultUtil.buildSuccessResult("验证码已下发");
            }
        });
    }

    /**
     * 注册空白新用户
     *
     * @param request
     * @param httpServletRequest
     * @param company
     * @return
     */
    @PostMapping(value = "/register")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserVO> register(UserRequestBase request, HttpServletRequest httpServletRequest,
                                   @RequestParam(value = "company", required = false) String company,
                                   @RequestParam(value = "smsCode", required = false) String smsCode) {
        return RestOperateTemplate.operate(LOGGER, "用户注册", request, new RestOperateCallBack<UserVO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
                AssertUtil.assertStringNotBlank(request.getPassword(), UserErrorCode.INVALID_PASSWORD);
                AssertUtil.assertStringNotBlank(company, RestResultCode.ILLEGAL_PARAMETERS, "公司信息不能为空");
                AssertUtil.assertStringNotBlank(smsCode, RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
                VerificationCodeUtil.validate(request.getUsername(), smsCode);
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<UserVO> execute() {
                UserInfoBO userInfoBO = new UserInfoBO();
                userInfoBO.setRealNameCheck(0);
                userInfoBO.setCompany(company);
                userInfoBO.setExtInfo(new HashMap<>(0));
                userInfoBO.setSex("未知");
                userInfoBO.setMobilePhone(request.getUsername());

                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername())
                        .withPassword(request.getPassword())
                        .withUserInfoBO(userInfoBO);
                OperateContext operateContext = new OperateContext();
                operateContext.setOperateIp(IPUtil.getIp(httpServletRequest));
                UserVO userVO = UserVOConverter.convert(userService.register(commonUserRequestBuilder.build(), operateContext));
                return RestResultUtil.buildSuccessResult(userVO, "注册成功");
            }
        });
    }

    /**
     * 用户信息登记
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/info")
    @ValidateToken
    @Limit(threshold = 10)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<String> updateUserInfo(UserInfoRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "更新用户信息", null, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotEquals(request.getNickname() + request.getRealName() + request.getSex()
                                + request.getMobilePhone() + request.getEmail() + request.getBirthday() + request.getCompany(),
                        "nullnullnullnullnullnullnull", "未获取到更新信息");
                AssertUtil.assertTrue(!SensitiveFilterUtil.contains(request.toAuditString()), RestResultCode.AUDIT_HIT);
            }

            @Override
            public Result<String> execute() {
                UserInfoBO userInfoBO = new UserInfoBO();
                userInfoBO.setUserId(request.getUserId());
                userInfoBO.setNickname(request.getNickname());
                userInfoBO.setRealName(request.getRealName());
                userInfoBO.setSex(request.getSex());
                userInfoBO.setMobilePhone(request.getMobilePhone());
                userInfoBO.setEmail(request.getEmail());
                userInfoBO.setBirthday(request.getBirthday() != null ?
                        DateUtil.getDateByYearMonthDayStr(request.getBirthday()) : null);
                userInfoBO.setCompany(request.getCompany());

                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withUserId(request.getUserId())
                        .withUserInfoBO(userInfoBO);
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                CommonUserRequest commonUserRequest = commonUserRequestBuilder.build();
                userService.updateUserInfo(commonUserRequest, context);
                return RestResultUtil.buildSuccessResult("信息登记成功");
            }
        });
    }

    /**
     * 用户头像登记
     *
     * @param file
     * @return
     */
    @PutMapping(value = "/avatar")
    @ValidateToken
    @Limit(threshold = 10)
    public Result<String> updateUserAvatar(UserRequestBase request, HttpServletRequest httpServletRequest,
                                           @RequestParam(value = "avatar", required = false) MultipartFile file) {
        return RestOperateTemplate.operate(LOGGER, "更新头像", httpServletRequest, new RestOperateCallBack<String>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(file, RestResultCode.ILLEGAL_PARAMETERS, "上传头像不能为空");
                //AssertUtil.assertTrue(Objects.equals(file.getContentType(), allowAvatarType), RestResultCode.ILLEGAL_PARAMETERS, "头像文件类型非法");
                AssertUtil.assertTrue(file.getSize() != 0, "非法空文件");
                AssertUtil.assertTrue(file.getSize() <= maxAvatarSize, "头像文件大小超限");
            }

            @Override
            public Result<String> execute() {
                CommonUserRequestBuilder builder = CommonUserRequestBuilder.getInstance()
                        .withUserId(request.getUserId());

                userService.avatarUpdate(builder.build(), FileUtil.multipartFileToFile(file));

                return RestResultUtil.buildSuccessResult("更新头像成功");
            }
        });
    }

    /**
     * 获取密码重置短信验证码
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/password/reset")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Object> getResetSmsCode(UserRequestBase request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户请求密码重置短信验证", null, new RestOperateCallBack<Object>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(httpServletRequest, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
            }

            @Override
            public Result<Object> execute() {
                verificationCodeUtil.initResetSmsCode(request.getUsername());
                return RestResultUtil.buildSuccessResult("验证码已下发");
            }
        });
    }

    /**
     * 重置密码
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping(value = "/password/reset")
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Object> resetPassword(UserRequestBase request, HttpServletRequest httpServletRequest,
                                        @RequestParam(value = "smsCode", required = false) String smsCode) {
        return RestOperateTemplate.operate(LOGGER, "重置密码", null, new RestOperateCallBack<Object>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS);
                AssertUtil.assertStringNotBlank(request.getUsername(), UserErrorCode.INVALID_USERNAME);
                AssertUtil.assertTrue(verificationCodeUtil.validateMobile(request.getUsername()), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
                AssertUtil.assertStringNotBlank(request.getNewPassword(), RestResultCode.ILLEGAL_PARAMETERS, "新密码不能为空");
                AssertUtil.assertStringNotBlank(smsCode, RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
                VerificationCodeUtil.validate(request.getUsername(), smsCode);
            }

            @Override
            public Result<Object> execute() {
                CommonUserRequestBuilder commonUserRequestBuilder = CommonUserRequestBuilder.getInstance()
                        .withRequestId(request.getRequestId())
                        .withUsername(request.getUsername());
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));

                CommonUserRequest commonUserRequest = commonUserRequestBuilder.build();
                commonUserRequest.putExtInfo(UserRequestExtInfoKey.USER_NEW_PASSWORD, request.getNewPassword());

                userService.resetPassword(commonUserRequest, context);
                return RestResultUtil.buildSuccessResult("密码重置成功");
            }
        });
    }


    /*查询所有用户信息*/
    @GetMapping("/selectAll")
    public Result<List<UserInfoBO>> selectAll(){
        List<UserInfoBO> list = new ArrayList<>();
        List<UserInfoDO> userInfoDOS = userService.selectAll();
        for (UserInfoDO userInfoDO : userInfoDOS){
            list.add(EntityConverter.convert(userInfoDO));
        }
        return RestResultUtil.buildSuccessResult(list,"查询成功");
    }
}