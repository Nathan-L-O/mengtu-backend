package com.mengtu.kaichi.user.manager.impl;

import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.dal.service.RoleRepoService;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.dal.service.UserRepoService;
import com.mengtu.kaichi.user.manager.UserManager;
import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.kaichi.user.user.builder.UserBOBuilder;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.LoggerUtil;
import com.mengtu.util.validator.MultiValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/22 15:32
 */
@Service
public class UserManagerImpl implements UserManager {

    private final Logger LOGGER = LoggerFactory.getLogger(UserManagerImpl.class);

    @Resource
    private UserRepoService userRepoService;

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private RoleRepoService roleRepoService;

    @Resource
    private PermRepoService permRepoService;

    @Resource
    private MultiValidator<UserManageRequest> userRegisterValidator;

    @Resource
    private ObsUtil obsUtil;

    private static final String AVATAR_LOCATION = "user/avatar/";

    @Override
    @Transactional
    public CommonUser create(UserManageRequest request) {
        // 校验用户是否合法
        userRegisterValidator.validate(request);

        // 创建用户
        UserBOBuilder userBOBuilder = UserBOBuilder.getInstance(request.getUsername(), request.getPassword())
                .withSalt(request.getSalt());
        UserBO user = userRepoService.createUser(userBOBuilder.build());

        // 绑定用户信息
        UserInfoBO userInfo = request.getUserInfoBO();
        if (userInfo != null) {
            userInfoRepoService.bindUserInfo(user.getUserId(), userInfo);
            try {
                obsUtil.copy(obsUtil.fetchFile(AVATAR_LOCATION, "1qaz2wsx"),
                        AVATAR_LOCATION + HashUtil.sha256(user.getUserId()) + ".svg");
            } catch (Exception ignored) {
            }

        } else {
            LoggerUtil.warn(LOGGER, "创建时没有绑定用户信息 UserManageRequest={0}", request);
        }

        // 绑定角色
        List<RoleBO> userRoles = roleRepoService.userBindRoles(user.getUserId(), request.getRoleIds());

        // 绑定权限
        permRepoService.userBindPerms(user.getUserId(), request.getPermIds());

        // 构建结果
        CommonUser commonUser = new CommonUser();
        commonUser.setUserId(user.getUserId());
        commonUser.setUserInfo(userInfo);
        userRoles.forEach(commonUser::putRole);
        return commonUser;
    }

    @Override
    public void batchBindRole(UserManageRequest request) {
        roleRepoService.userBindRoles(request.getUserId(), request.getRoleIds());
    }

    @Override
    public void batchBindRolByCode(UserManageRequest request) {
        roleRepoService.userBindRolesByCode(request.getUserId(), request.getRoleCode());
    }

    @Override
    public void batchUnbindRole(UserManageRequest request) {
        roleRepoService.userUnbindRoles(request.getUserId(), request.getRoleIds());
    }

    @Override
    public void batchBindPerm(UserManageRequest request) {
        permRepoService.userBindPerms(request.getUserId(), request.getPermIds());
    }

    @Override
    public void batchUnbindPerm(UserManageRequest request) {
        permRepoService.userUnbindPerms(request.getUserId(), request.getPermIds());
    }
}
