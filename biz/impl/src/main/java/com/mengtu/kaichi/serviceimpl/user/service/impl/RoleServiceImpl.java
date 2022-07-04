package com.mengtu.kaichi.serviceimpl.user.service.impl;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.user.enums.UserRoleCode;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.serviceimpl.user.service.RoleService;
import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.model.perm.UserRoleRelationDO;
import com.mengtu.kaichi.user.dal.repo.perm.RoleDORepo;
import com.mengtu.kaichi.user.dal.repo.perm.UserRoleRelationDORepo;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.manager.RoleManager;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.model.basic.perm.UserRoleRelationBO;
import com.mengtu.kaichi.user.request.RoleManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:17
 */
@Service
public class RoleServiceImpl implements RoleService {

    /**
     * 创建角色 默认role_code
     */
    private static final String ROLE_CODE = "MANAGE_CREATE";


    @Resource
    private RoleManager roleManager;
    @Resource
    private RoleDORepo roleDORepo;
    @Resource
    private UserInfoRepoService userInfoRepoService;
    @Resource
    private UserRoleRelationDORepo userRoleRelationDORepo;

    @Override
    public List<RoleBO> findAllRole() {
        return roleManager.findAllRole();
    }

    @Override
    public RoleBO createRole(RoleUserPermRequest request, OperateContext context) {
        RoleBO roleBO = new RoleBO();
        roleBO.setRoleName(request.getRoleName());
        roleBO.setRoleDesc(request.getRoleDescribe());
        roleBO.setRoleCode(ROLE_CODE);

        RoleManageRequest roleManageRequest = new RoleManageRequest();
        roleManageRequest.setRole(roleBO);
        if (request.getPermIds() != null) {
            roleManageRequest.setPermIds(request.getPermIds());
        }
        if (!request.getUserIds().isEmpty()) {
            roleManageRequest.setUserIds(request.getUserIds());
        }
        return roleManager.createRole(roleManageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindUsers(RoleUserPermRequest request, OperateContext context) {
        List<String> useIds = new ArrayList<>();
        List<String> stuids = request.getUserIds();
//        List<String> stuids = request.getStuIds();
        //获取useid
        String userId;
        for (String stuid : stuids) {
            userId = userInfoRepoService.queryUserInfoByUserId(stuid).getUserId();
            AssertUtil.assertNotNull(userId);
            useIds.add(userId);
        }
//        for (String stuid : stuids) {
//            userId = userInfoRepoService.queryUserInfoByStuId(stuid).getUserId();
//            AssertUtil.assertNotNull(userId);
//            useIds.add(userId);
//        }
        //获取角色
        RoleManageRequest roleManageRequest = new RoleManageRequest();
        RoleBO roleBO = EntityConverter.convert(roleDORepo.findByRoleId(request.getRoleId()));
        roleManageRequest.setRole(roleBO);
        roleManageRequest.setUserIds(useIds);
        roleManager.batchUsersBindRole(roleManageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindUsers(RoleUserPermRequest request, OperateContext context) {
        List<String> useids = request.getUserIds();
        List<String> stuids = request.getUserIds();
        String userId;
        if (useids == null && stuids == null) {
            AssertUtil.assertNotNull("解绑用户主体不存在");
        }
        if (useids == null) {
            useids = new ArrayList<String>();
        }
        if (stuids.size() != 0) {
            for (int i = 0; i < stuids.size(); i++) {
                userId = userInfoRepoService.queryUserInfoByUserId(stuids.get(i)).getUserId();
//                userId = userInfoRepoService.queryUserInfoByStuId(stuids.get(i)).getUserId();
                AssertUtil.assertNotNull(userId);
                useids.add(userId);
            }
        }
        RoleBO roleBO = EntityConverter.convert(roleDORepo.findByRoleId(request.getRoleId()));
        RoleManageRequest roleManageRequest = new RoleManageRequest();
        roleManageRequest.setUserIds(useids);
        roleManageRequest.setRole(roleBO);
        roleManager.batchUsersUnbindRole(roleManageRequest);
    }

    @Override
    public List<UserRoleRelationBO> findAllUserRelationRole() {
        //过滤掉活动盖章员和证书审核员
        String activityStampRoleId = roleDORepo.findByRoleCode(UserRoleCode.CERTIFICATE_CONFIRM.getCode()).getRoleId();
        String certificateRoleId = roleDORepo.findByRoleCode(UserRoleCode.ACTIVITY_STAMPER.getCode()).getRoleId();
        List<UserRoleRelationDO> userRoleRelationDOS = userRoleRelationDORepo.findAll();
        List<UserRoleRelationBO> userRoleRelationBOList = CollectionUtil.toStream(userRoleRelationDOS)
                .filter(Objects::nonNull)
                .filter(userRoleRelationDO -> !(userRoleRelationDO.getRoleId().equals(activityStampRoleId)))
                .filter(userRoleRelationDO -> !(userRoleRelationDO.getRoleId().equals(certificateRoleId)))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
        for (int i = 0; i < userRoleRelationBOList.size(); i++) {
            String name = userInfoRepoService.queryUserInfoByUserId(userRoleRelationBOList.get(i).getUserId()).getRealName();
            userRoleRelationBOList.get(i).setUserName(name);
            String rolename = roleDORepo.findByRoleId(userRoleRelationBOList.get(i).getRoleId()).getRoleName();
            userRoleRelationBOList.get(i).setRoleName(rolename);
        }
        return userRoleRelationBOList;
    }
}
