package com.mengtu.kaichi.controller.user;

import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.model.user.request.RoleUserPermRestRequest;
import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.serviceimpl.user.service.PermService;
import com.mengtu.kaichi.user.manager.PermManager;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.util.IPUtil;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 权限控制器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 11:16
 * @return null
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/perm", produces = {"application/json;charset=UTF-8"})
public class PermController {
    /**
     * 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(com.mengtu.kaichi.controller.user.PermController.class);

    @Resource
    PermManager permManager;
    @Resource
    PermService permService;

    /**
     * 创建权限
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<PermBO> createPerm(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "创建权限", request, new RestOperateCallBack<PermBO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertNotNull(request.getPermName(), RestResultCode.ILLEGAL_PARAMETERS, "权限名字不存在");
                AssertUtil.assertNotNull(request.getDescribe(), RestResultCode.ILLEGAL_PARAMETERS, "权限描述不存在");
            }

            @Override
            public Result<PermBO> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setPermName(request.getPermName());
                roleUserPermRequest.setPermDescribe(request.getDescribe());
                roleUserPermRequest.setExtInfo(request.getExtInfo());
                return RestResultUtil.buildSuccessResult(permService.createPerm(roleUserPermRequest, context), "创建权限成功");
            }
        });
    }


    /**
     * 获取用户的权限
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<PermBO>> getPerms(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取权限", request, new RestOperateCallBack<List<PermBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
            }

            @Override
            public Result<List<PermBO>> execute() {
                List<String> userIdList = new ArrayList<>();
                userIdList.add(request.getUserId());
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setUserIds(userIdList);

                return RestResultUtil.buildSuccessResult(permService.findPerm(roleUserPermRequest), "获取权限成功");
            }
        });
    }

    /**
     * 获取所有的权限(不包括组织管理员)
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/all")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<PermBO>> getAllPerms(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取全部权限", request, new RestOperateCallBack<List<PermBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
            }

            @Override
            public Result<List<PermBO>> execute() {
                return RestResultUtil.buildSuccessResult(permService.findAllNotContainOrganization(), "获取所有权限成功");
            }
        });
    }


    /**
     * 获取权限的所有用户
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/users")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserInfoBO>> getPermUsers(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取权限的所有用户", request, new RestOperateCallBack<List<UserInfoBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertNotNull(request.getPermId(), RestResultCode.ILLEGAL_PARAMETERS, "权限id不存在");
            }

            @Override
            public Result<List<UserInfoBO>> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setPermId(request.getPermId());
                permService.getPermUsers(roleUserPermRequest, context);
                return RestResultUtil.buildSuccessResult(permService.getPermUsers(roleUserPermRequest, context), "获取该权限所有用户成功");
            }
        });
    }

    /**
     * 批量用户解绑权限
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/permUsers")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result batchUsersUnbindPerms(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "批量用户解绑权限", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertNotNull(request.getPermId(), RestResultCode.ILLEGAL_PARAMETERS, "权限id不存在");
                AssertUtil.assertNotNull(request.getUsernames(), RestResultCode.ILLEGAL_PARAMETERS, "解绑用户 ID 不存在");
            }

            @Override
            public Result execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setPermId(request.getPermId());
                roleUserPermRequest.setUserIds(request.getUsernames());
                permService.batchUsersUnbindPerms(roleUserPermRequest, context);
                return RestResultUtil.buildSuccessResult("解绑用户成功");
            }
        });
    }

    /**
     * 批量用户绑定权限
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/permUsers")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserInfoBO>> batchUsersBindPerms(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "批量用户绑定权限", request, new RestOperateCallBack<List<UserInfoBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertNotNull(request.getPermId(), RestResultCode.ILLEGAL_PARAMETERS, "权限id不存在");
                AssertUtil.assertNotNull(request.getUsernames(), RestResultCode.ILLEGAL_PARAMETERS, "绑定的权限用户id不能为空");
            }

            @Override
            public Result<List<UserInfoBO>> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setPermId(request.getPermId());
                roleUserPermRequest.setUserIds(request.getUsernames());
                return RestResultUtil.buildSuccessResult(permService.batchUsersBindPerms(roleUserPermRequest, context), "绑定权限成功");
            }
        });
    }


    /**
     * 权限和所有用户解绑
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping("/detachAllUsers")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result detachAllUsers(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "解绑权限所有用户", request, new RestOperateCallBack<Result>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertStringNotBlank(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户不能为空");
                AssertUtil.assertNotNull(request.getPermId(), RestResultCode.ILLEGAL_PARAMETERS, "权限id不存在");
            }

            @Override
            public Result execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleUserPermRequest = new RoleUserPermRequest();
                roleUserPermRequest.setPermId(request.getPermId());
                permService.detachAllUsers(roleUserPermRequest, context);
                return RestResultUtil.buildSuccessResult("解绑权限所有用户成功");
            }
        });
    }
}
