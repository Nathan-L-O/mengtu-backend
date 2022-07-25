package com.mengtu.kaichi.serviceimpl.user.service.impl;

import com.mengtu.kaichi.serviceimpl.common.OperateContext;
import com.mengtu.kaichi.serviceimpl.common.constant.UserRequestExtInfoKey;
import com.mengtu.kaichi.serviceimpl.user.request.CommonUserRequest;
import com.mengtu.kaichi.serviceimpl.user.service.UserService;
import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import com.mengtu.kaichi.user.dal.service.UserRepoService;
import com.mengtu.kaichi.user.manager.UserManager;
import com.mengtu.kaichi.user.mapper.UserMapper;
import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.kaichi.user.util.EncryptUtil;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.UUIDUtil;
import com.mengtu.util.validator.MultiValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 用户服务实现
 *
 * @author 过昊天
 * @version 1.3 @ 2022/4/22 15:00
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserBasicService userBasicService;

    @Resource
    private UserRepoService userRepoService;

    @Resource
    private UserManager userManager;

    @Resource
    private ObsUtil obsUtil;

    @Resource
    private MultiValidator<UserManageRequest> passwordValidator;

    @Resource
    private MultiValidator<UserManageRequest> userRegisterValidator;

    @Resource
    private UserMapper userMapper;

    /**
     * 头像 OBS 地址
     */
    private static final String AVATAR_LOCATION = "user/avatar/";

    @Override
    public CommonUser login(CommonUserRequest request, OperateContext context) {
        return userBasicService.login(request.getUsername(), request.getPassword(), context.getOperateIp());
    }

    @Override
    public CommonUser smsLogin(CommonUserRequest request, OperateContext context) {
        return userBasicService.smsLogin(request.getUsername(), context.getOperateIp());
    }

    @Override
    public CommonUser fetchUser(CommonUserRequest request, OperateContext context) {
        return userBasicService.getByUserId(request.getUserId());
    }

    @Override
    public void logout(CommonUserRequest request, OperateContext context) {
        userBasicService.logOut(request.getUserId());
    }

    @Override
    public void modifyUser(CommonUserRequest request, OperateContext context) {

        UserBO userBO = userRepoService.queryByUserId(request.getUserId());
        AssertUtil.assertNotNull(userBO, "用户不存在");

        if (StringUtils.isNotBlank(request.fetchExtInfo(UserRequestExtInfoKey.USER_NEW_PASSWORD))) {
            if (StringUtils.isNotBlank(request.getPassword())) {
                String oldEncodePwd = EncryptUtil.encryptPassword(request.getPassword(), userBO.getSalt());
                AssertUtil.assertTrue(StringUtils.equals(userBO.getPassword(), oldEncodePwd), "旧密码错误");
            }

            request.setPassword(request.fetchExtInfo(UserRequestExtInfoKey.USER_NEW_PASSWORD));
            passwordValidator.validate(request);

            userBO.setSalt(UUIDUtil.generate(true));
            userBO.setPassword(EncryptUtil.encryptPassword(request.getPassword(), userBO.getSalt()));
        }
        userRepoService.updateUserByUserId(userBO);
    }

    @Override
    public void updateUserInfo(CommonUserRequest request, OperateContext context) {
        userBasicService.updateUserInfo(request.getUserId(),
                request.getUserInfoBO().getNickname(),
                request.getUserInfoBO().getRealName(),
                request.getUserInfoBO().getSex(),
                request.getUserInfoBO().getMobilePhone(),
                request.getUserInfoBO().getEmail(),
                request.getUserInfoBO().getBirthday(),
                request.getUserInfoBO().getCompany());
    }

    @Override
    public void resetPassword(CommonUserRequest request, OperateContext context) {

        UserBO userBO = userRepoService.queryByUserName(request.getUsername());
        AssertUtil.assertNotNull(userBO, "用户不存在");

        String newPassword = request.fetchExtInfo(UserRequestExtInfoKey.USER_NEW_PASSWORD);
        AssertUtil.assertStringNotBlank(newPassword, "新密码不能为空");

        request.setPassword(newPassword);
        passwordValidator.validate(request);
        userBO.setSalt(UUIDUtil.generate(true));
        userBO.setPassword(EncryptUtil.encryptPassword(request.getPassword(), userBO.getSalt()));

        userRepoService.updateUserByUserId(userBO);
    }

    @Override
    public CommonUser register(CommonUserRequest request, OperateContext context) {
        registerValidate(request);
        return userManager.create(request);
    }

    @Override
    public void registerValidate(CommonUserRequest request) {
        userRegisterValidator.validate(request);
    }

    @Override
    public CommonUser injectAvatarUrl(CommonUser commonUser) {
        try {
            UserInfoBO userInfoBO = commonUser.getUserInfo();
            Map<String, String> extInfo = userInfoBO.getExtInfo();
            extInfo.put("avatarUrl", obsUtil.getSignatureDownloadUrl(AVATAR_LOCATION, HashUtil.sha256(commonUser.getUserId()), 120L));
            userInfoBO.setExtInfo(extInfo);
            commonUser.setUserInfo(userInfoBO);
        } catch (Exception ignored) {
        }
        return commonUser;
    }

    @Override
    public void avatarUpdate(CommonUserRequest request, File avatarFile) {
        String hash = HashUtil.sha256(request.getUserId());
        String originalFileName = avatarFile.getName();
        try {
            obsUtil.deleteFile(AVATAR_LOCATION, hash);
        } catch (Exception ignored) {
        }
        obsUtil.uploadFile(avatarFile, AVATAR_LOCATION, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));
    }

    @Override
    public List<UserInfoDO> selectAll() {
        return userMapper.selectList(null);
    }

}
