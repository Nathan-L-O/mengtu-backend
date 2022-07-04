package com.mengtu.kaichi.user.user.service.impl;

import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.dal.service.UserRepoService;
import com.mengtu.kaichi.user.enums.UserErrorCode;
import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.user.helper.UserHelper;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.kaichi.user.util.EncryptUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.LoggerUtil;
import com.mengtu.util.tools.UUIDUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户基础服务实现
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/5 09:16
 */
@Service
public class UserBasicServiceImpl implements UserBasicService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserBasicServiceImpl.class);

    @Resource
    private UserRepoService userRepoService;

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private UserHelper userHelper;

    @Resource
    private PermRepoService permRepoService;

    @Override
    public CommonUser login(String username, String password, String loginIp) {
        UserBO userBO = userRepoService.queryByUserName(username);
        AssertUtil.assertNotNull(userBO, UserErrorCode.INCORRECT_USERNAME_OR_PASSWORD);
        AssertUtil.assertTrue(StringUtils.equals(EncryptUtil.encryptPassword(password, userBO.getSalt()), userBO.getPassword()), UserErrorCode.INCORRECT_USERNAME_OR_PASSWORD);

        return getCommonUser(loginIp, userBO);
    }

    @Override
    public CommonUser smsLogin(String username, String loginIp) {
        UserBO userBO = userRepoService.queryByUserName(username);
        AssertUtil.assertNotNull(userBO, UserErrorCode.INCORRECT_USERNAME_OR_PASSWORD);

        return getCommonUser(loginIp, userBO);
    }

    @Override
    public CommonUser getByUserId(String userId) {
        CommonUser user = new CommonUser();
        user.setUserId(userId);
        userHelper.fillUserInfo(user);

        if (user.getUserInfo() == null) {
            LoggerUtil.warn(LOGGER, "用户还未绑定用户信息");
        }

        return user;
    }

    @Override
    public UserBO checkLogin(String token, String loginIp) {
        String sessionId = EncryptUtil.parseToken(token);
        UserBO userBO = userRepoService.queryBySessionId(sessionId);
        if (userBO == null) {
            LoggerUtil.info(LOGGER, "用户登陆失效, token={0}, sessionId={1}, IP={2}", token, sessionId, loginIp);
            return null;
        }

        if (StringUtils.isBlank(loginIp)) {
            LoggerUtil.warn(LOGGER, "未知 IP 用户登陆");
        }

        userBO.setLastLoginIp(loginIp);
        userBO.setLastLoginDate(new Date());
        userRepoService.updateUserByUserId(userBO);
        return userBO;
    }

    @Override
    public void logOut(String userId) {
        userRepoService.clearSessionId(userId);
    }

    @Override
    public void updateUserInfo(String userId, String nickname, String realName, String sex, String mobilePhone, String email, Date birthday, String company) {
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setNickname(nickname);
        userInfoBO.setRealName(realName);
        userInfoBO.setSex(sex);
        userInfoBO.setMobilePhone(mobilePhone);
        userInfoBO.setEmail(email);
        userInfoBO.setBirthday(birthday);
        userInfoBO.setCompany(company);

        userInfoRepoService.modifyUserInfoByUserId(userId, userInfoBO);
    }

    @Override
    public boolean verifyPermissionByPermId(String userId, List<String> permIds) {
        // 判断用户上是否存在权限
        if (permRepoService.verifyUserPermRelationByPermId(userId, permIds)) {
            return true;
        }
        // 判断用户的角色上是否存在权限
        List<String> roleIds = new ArrayList<>(userHelper.fetchUserRoles(userId).keySet());
        for (String roleId : roleIds) {
            if (permRepoService.verifyRolePermRelationByPermId(roleId, permIds)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean verifyPermissionByPermType(String userId, List<String> permTypes) {

        // 判断用户上是否存在权限
        if (permRepoService.verifyUserPermRelationByPermType(userId, permTypes)) {
            return true;
        }
        // 判断用户的角色上是否存在权限
        List<String> roleIds = new ArrayList<>(userHelper.fetchUserRoles(userId).keySet());
        for (String roleId : roleIds) {
            if (permRepoService.verifyRolePermRelationByPermType(roleId, permTypes)) {
                return true;
            }
        }
        return false;
    }

    private CommonUser getCommonUser(String loginIp, UserBO userBO) {
        if (StringUtils.isBlank(loginIp)) {
            LoggerUtil.warn(LOGGER, "未知 IP 用户登陆");
        }

        String token = UUIDUtil.generate(false);
        userBO.setLastLoginIp(loginIp);
        userBO.setLastLoginDate(new Date());
        userBO.setSessionId(EncryptUtil.parseToken(token));
        userRepoService.updateUserByUserId(userBO);

        CommonUser user = getByUserId(userBO.getUserId());
        user.setToken(token);
        return user;
    }

}
