package com.mengtu.kaichi.controller.user;

import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.model.user.request.RoleUserPermRestRequest;
import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.serviceimpl.user.service.RoleService;
import com.mengtu.kaichi.user.manager.RoleManager;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.model.basic.perm.UserRoleRelationBO;
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
import java.util.List;

/**
 * 角色接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 11:28
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/role", produces = {"application/json;charset=UTF-8"})
public class RoleController {
    /**
     * 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    @Resource
    RoleService roleService;

    @Resource
    RoleManager roleManager;

    /**
     * 创建角色
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<RoleBO> createRole(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "创建角色", request, new RestOperateCallBack<RoleBO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertNotNull(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户id不能为空");
                AssertUtil.assertNotNull(request.getRoleName(), RestResultCode.ILLEGAL_PARAMETERS, "创建角色名字不存在");
            }

            @Override
            public Result<RoleBO> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleRequest = new RoleUserPermRequest();
                roleRequest.setRoleName(request.getRoleName());

                if (request.getDescribe() != null) {
                    roleRequest.setRoleDescribe(request.getDescribe());
                }
                if (request.getUserIds() != null) {
                    roleRequest.setUserIds(request.getUserIds());
                }
                if (request.getPermIds() != null) {
                    roleRequest.setPermIds(request.getPermIds());
                }

                return RestResultUtil.buildSuccessResult(roleService.createRole(roleRequest, context), "创建角色成功");
            }
        });
    }


    /**
     * 获取所有角色信息
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/roles")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<RoleBO>> getAllUserRole(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取角色列表", request, new RestOperateCallBack<List<RoleBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertNotNull(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户id不能为空");
            }

            @Override
            public Result<List<RoleBO>> execute() {
                return RestResultUtil.buildSuccessResult(roleService.findAllRole(), "获取角色列表成功");
            }
        });
    }


    /**
     * 用户绑定角色
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/userBatchRole")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<RoleBO> userBatchRole(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户绑定角色", request, new RestOperateCallBack<RoleBO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertNotNull(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户id不能为空");
                AssertUtil.assertNotNull(request.getUsernames(), RestResultCode.ILLEGAL_PARAMETERS, "赋权角色 ID 不能为空");
                AssertUtil.assertNotNull(request.getRoleId(), RestResultCode.ILLEGAL_PARAMETERS, "赋权角色id不能为空");
            }

            @Override
            public Result<RoleBO> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleRequest = new RoleUserPermRequest();
                roleRequest.setRoleId(request.getRoleId());
                roleRequest.setUserIds(request.getUsernames());
                roleService.bindUsers(roleRequest, context);
                return RestResultUtil.buildSuccessResult("绑定用户角色成功");
            }
        });
    }

    /**
     * 获取所有用户以及所有用户的角色
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "roleUserRelation")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<UserRoleRelationBO>> getUserRoleRelation(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "获取所有用户以及所有用户的角色", request, new RestOperateCallBack<List<UserRoleRelationBO>>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertNotNull(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户id不能为空");
            }

            @Override
            public Result<List<UserRoleRelationBO>> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                List<UserRoleRelationBO> userRoleRelationBOList = roleService.findAllUserRelationRole();
                return RestResultUtil.buildSuccessResult(userRoleRelationBOList, "获取所有用户以及所有用户的角色");
            }
        });
    }

    /**
     * 解绑用户和角色关系
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping(value = "roleUserRelation")
    @ValidateToken
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<UserRoleRelationBO> unbatchUserRoleRelation(RoleUserPermRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "用户绑定角色", request, new RestOperateCallBack<UserRoleRelationBO>() {
            @Override
            public void before() {
                AssertUtil.assertNotNull(request, RestResultCode.ILLEGAL_PARAMETERS, "请求体不能为空");
                AssertUtil.assertNotNull(request.getUserId(), RestResultCode.ILLEGAL_PARAMETERS, "用户id不能为空");
                AssertUtil.assertNotNull(request.getRoleId(), RestResultCode.ILLEGAL_PARAMETERS, "解绑角色id不能为空");
            }

            @Override
            public Result<UserRoleRelationBO> execute() {
                OperateContext context = new OperateContext();
                context.setOperateIp(IPUtil.getIp(httpServletRequest));
                RoleUserPermRequest roleRequest = new RoleUserPermRequest();
                roleRequest.setUserIds(request.getUserIds());
                roleRequest.setRoleId(request.getRoleId());
                roleRequest.setUserIds(request.getUsernames());
                roleService.unbindUsers(roleRequest, context);
                return RestResultUtil.buildSuccessResult("解绑用户角色关系");
            }
        });
    }

}
