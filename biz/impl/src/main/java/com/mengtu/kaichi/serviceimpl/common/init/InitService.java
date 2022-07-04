package com.mengtu.kaichi.serviceimpl.common.init;

import com.mengtu.kaichi.organization.manager.OrganizationManager;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermType;
import com.mengtu.kaichi.serviceimpl.organization.enums.OrganizationPermTypeEnum;
import com.mengtu.kaichi.serviceimpl.user.constant.GeneralPermType;
import com.mengtu.kaichi.serviceimpl.user.constant.UserPermType;
import com.mengtu.kaichi.serviceimpl.user.enums.GeneralManagerPermTypeEnum;
import com.mengtu.kaichi.serviceimpl.user.enums.UserManagerPermTypeEnum;
import com.mengtu.kaichi.serviceimpl.user.enums.UserRoleCode;
import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.dal.service.RoleRepoService;
import com.mengtu.kaichi.user.enums.PermType;
import com.mengtu.kaichi.user.enums.RoleCode;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.user.builder.PermBOBuilder;
import com.mengtu.kaichi.user.user.builder.RoleBOBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 初始化设置
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/20 16:06
 */
@Service
public class InitService {

    @Resource
    private PermRepoService permRepoService;

    @Resource
    private RoleRepoService roleRepoService;

    @Resource
    private OrganizationManager organizationManager;


    private final static List<String> ORGANIZATION_MANAGER_PERM = new ArrayList<>();

    private final static List<String> USER_MANAGER_PERM = new ArrayList<>();

    private final static List<String> GENERAL_MANAGER_PERM = new ArrayList<>();

    static {
        //组织
        ORGANIZATION_MANAGER_PERM.add(OrganizationPermType.ALL_ORG_MANAGE);
        ORGANIZATION_MANAGER_PERM.add(OrganizationPermType.ALL_ORG_MEMBER_MANAGE);
        ORGANIZATION_MANAGER_PERM.add(OrganizationPermType.ALL_ORG_RELATION_MANAGE);

        //用户
        USER_MANAGER_PERM.add(UserPermType.USER_PASSWORD_RESET);

        //总管理
        GENERAL_MANAGER_PERM.add(GeneralPermType.PERM_OPERATOR);
    }


    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    public void init() {

        //初始化权限
        Map<String, String> initPermMap = new HashMap<>(16);
        PermBOBuilder permBuilder = PermBOBuilder.getInstance();

        //组织权限
        for (PermType permType : OrganizationPermTypeEnum.values()) {
            if (permType.isInit()) {
                permBuilder.withPermType(permType.getCode())
                        .withPermName(permType.getDesc());
                initPermMap.put(permType.getCode(), permRepoService.initPerm(permBuilder.build()).getPermId());
            }
        }

        //管理用户权限
        for (PermType permType : UserManagerPermTypeEnum.values()) {
            if (permType.isInit()) {
                permBuilder.withPermType(permType.getCode())
                        .withPermName(permType.getDesc());
                initPermMap.put(permType.getCode(), permRepoService.initPerm(permBuilder.build()).getPermId());
            }
        }

        // 初始化总管理
        for (PermType permType : GeneralManagerPermTypeEnum.values()) {
            if (permType.isInit()) {
                permBuilder.withPermType(permType.getCode())
                        .withPermName(permType.getDesc());
                initPermMap.put(permType.getCode(), permRepoService.initPerm(permBuilder.build()).getPermId());
            }
        }
        // 初始化角色
        RoleBOBuilder roleBOBuilder = RoleBOBuilder.getInstance();
        for (RoleCode roleCode : UserRoleCode.values()) {
            roleBOBuilder.withRoleCode(roleCode.getCode())
                    .withRoleName(roleCode.getDesc());
            RoleBO role = roleRepoService.initRole(roleBOBuilder.build());
            initRoleBindPerm(role, initPermMap);
        }
    }


    private void initRoleBindPerm(RoleBO role, Map<String, String> initPermMap) {

        // 初始化 组织管理员权限
        if (StringUtils.equals(role.getRoleCode(), UserRoleCode.ORGANIZATION_MANAGER.getCode())) {
            Set<String> permIds = new HashSet<>();
            ORGANIZATION_MANAGER_PERM.forEach(organizationPermType -> permIds.add(initPermMap.get(organizationPermType)));
            permRepoService.roleBindPermsUnbindNo(role.getRoleId(), new ArrayList<>(permIds));
        }
        //初始化 用户管理员权限
        if (StringUtils.equals(role.getRoleCode(), UserRoleCode.USER_MANAGER.getCode())) {
            Set<String> permId = new HashSet<>();
            USER_MANAGER_PERM.forEach(userPermType -> permId.add(initPermMap.get(userPermType)));
            permRepoService.roleBindPermsUnbindNo(role.getRoleId(), new ArrayList<>(permId));
        }
        //初始化 总管理权限
        if (StringUtils.equals(role.getRoleCode(), UserRoleCode.GENERAL_MANAGER.getCode())) {
            Set<String> permId = new HashSet<>();
            GENERAL_MANAGER_PERM.forEach(userPermType -> permId.add(initPermMap.get(userPermType)));
            permRepoService.roleBindPermsUnbindNo(role.getRoleId(), new ArrayList<>(permId));
        }
    }
}
