package com.mengtu.kaichi.serviceimpl.user.service.impl;

//import com.mengtu.kaichi.serviceimpl.activity.constant.ActivityPermType;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermType;
import com.mengtu.kaichi.serviceimpl.user.request.RoleUserPermRequest;
import com.mengtu.kaichi.serviceimpl.user.service.PermService;
import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.model.perm.PermDO;
import com.mengtu.kaichi.user.dal.model.perm.UserPermRelationDO;
import com.mengtu.kaichi.user.dal.repo.perm.PermDORepo;
import com.mengtu.kaichi.user.dal.repo.perm.UserPermRelationDORepo;
import com.mengtu.kaichi.user.dal.repo.perm.UserRoleRelationDORepo;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.manager.PermManager;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.request.PermManageRequest;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:12
 */
@Service
public class PermServiceImpl implements PermService {

    @Resource
    PermManager permManager;

    @Resource
    PermDORepo permDORepo;

    @Resource
    UserPermRelationDORepo userPermRelationDORepo;

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private UserRoleRelationDORepo userRoleRelationDORepo;

    @Resource
    private UserBasicService userBasicService;

    private static final String permType = "MANAGER_CREATE";

    @Override
    public PermBO createPerm(RoleUserPermRequest request, OperateContext context) {
        PermManageRequest permManageRequest = new PermManageRequest();
        PermBO permbo = new PermBO();
        permbo.setPermDesc(request.getPermDescribe());
        permbo.setPermName(request.getPermName());
        //权限类型设定为管理员创建
        permbo.setPermType(permType);
        permbo.setExtInfo(request.getExtInfo());
        permManageRequest.setPermBO(permbo);
        return permManager.createPerm(permManageRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<UserInfoBO> batchUsersBindPerms(RoleUserPermRequest request, OperateContext context) {
        List<String> userIds = request.getUserIds();
        List<UserInfoBO> userInfoBO = new ArrayList<>();

        for (String id : userIds) {
            userInfoBO.add(userInfoRepoService.queryUserInfoByUserId(id));
        }

        PermManageRequest permManageRequest = new PermManageRequest();
        permManageRequest.setUserIds(request.getUserIds());
        PermDO permDO = permDORepo.findByPermId(request.getPermId());
        AssertUtil.assertNotNull(permDO, "权限不存在");
        permManageRequest.setPermBO(EntityConverter.convert(permDO));
        permManager.batchUsersBindPerms(permManageRequest);

        return userInfoBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUsersUnbindPerms(RoleUserPermRequest request, OperateContext context) {
        List<String> useIds = new ArrayList<>();
        List<String> stuids = request.getUserIds();
        String userId;
        for (int i = 0; i < stuids.size(); i++) {
            userId = userInfoRepoService.queryUserInfoByUserId(stuids.get(i)).getUserId();
//            userId = userInfoRepoService.queryUserInfoByStuId(stuids.get(i)).getUserId();
            AssertUtil.assertNotNull(userId, "用户 ID 不存在：" + stuids.get(i) + "解绑失败");
            useIds.add(userId);
        }
        PermManageRequest permManageRequest = new PermManageRequest();
        permManageRequest.setUserIds(useIds);
        PermDO permDO = permDORepo.findByPermId(request.getPermId());
        AssertUtil.assertNotNull(permDO, "权限不存在");
        permManageRequest.setPermBO(EntityConverter.convert(permDO));
        permManager.batchUsersUnbindPerms(permManageRequest);
    }

    @Override
    public void detachAllUsers(RoleUserPermRequest request, OperateContext context) {
        String permId = request.getPermId();
        AssertUtil.assertNotNull(permDORepo.findByPermId(permId), "权限id不存在");
        permManager.detachAllUsers(request.getPermId());
    }

    @Override
    public List<UserInfoBO> getPermUsers(RoleUserPermRequest request, OperateContext context) {
        String permId = request.getPermId();
        AssertUtil.assertNotNull(permDORepo.findByPermId(permId), "权限id不存在");
        List<String> userIds = permManager.getPermUsers(permId);
        List<UserInfoBO> userInfoBOS = new ArrayList<>();
        for (String userId : userIds) {
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(userId);
            userInfoBOS.add(userInfoBO);
        }
        return userInfoBOS;
    }

    @Override
    public List<PermBO> findAllNotContainStamperAndFinance() {
        List<PermDO> permDos = permDORepo.findAll();
        return CollectionUtil.toStream(permDos)
                //.filter(permDO -> !(permDO.getPermType().equals(ActivityPermType.ACTIVITY_STAMPER)))
                //.filter(permDO -> !(permDO.getPermType().equals(FinancePermType.FINANCE_BAN)))
                //.filter(permDO -> !(permDO.getPermType().equals(FinancePermType.FINANCE_MANAGER)))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermBO> findAllNotContainOrganization() {
        List<PermDO> permDos = permDORepo.findAll();
        return CollectionUtil.toStream(permDos)
                .filter(permDO -> !(permDO.getPermType().equals(OrganizationPermType.ORG_MEMBER_MANAGE)))
                .filter(permDO -> !(permDO.getPermType().equals(OrganizationPermType.ORG_MEMBER_TYPE_MANAGE)))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermBO> findPerm(RoleUserPermRequest request) {
        List<PermDO> permDos = permDORepo.findAll();

        List<UserPermRelationDO> userPermRelations = userPermRelationDORepo.findAllByUserId(request.getUserIds().get(0));
        List<String> userPermList = new ArrayList<>();
        for (UserPermRelationDO userPermRelationDO : userPermRelations) {
            userPermList.add(userPermRelationDO.getPermId());
        }
        return CollectionUtil.toStream(permDos)
                .filter(permDO -> userPermList.contains(permDO.getPermId()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }


}
