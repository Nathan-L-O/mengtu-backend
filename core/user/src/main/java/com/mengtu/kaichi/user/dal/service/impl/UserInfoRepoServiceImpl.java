package com.mengtu.kaichi.user.dal.service.impl;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import com.mengtu.kaichi.user.dal.model.perm.UserDO;
import com.mengtu.kaichi.user.dal.repo.UserInfoDORepo;
import com.mengtu.kaichi.user.dal.repo.perm.UserDORepo;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.idfactory.BizIdFactory;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 用户信息仓储服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:34
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Service
public class UserInfoRepoServiceImpl implements UserInfoRepoService {

    @Resource
    private UserInfoDORepo userInfoDORepo;

    @Resource
    private UserDORepo userDORepo;

    @Resource
    private BizIdFactory userBizIdFactory;

    @Override
    public void bindUserInfo(String userId, UserInfoBO userInfoBO) {
        UserDO userDO = userDORepo.findByUserId(userId);
        AssertUtil.assertNotNull(userDO, "用户不存在");
        userInfoBO.setUserId(userId);

        UserInfoDO userInfoDO = userInfoDORepo.findByUserId(userId);
        AssertUtil.assertNull(userInfoDO, "用户已经绑定过用户信息，只能修改");

        if (StringUtils.isBlank(userInfoBO.getUserInfoId())) {
            userInfoBO.setUserInfoId(userBizIdFactory.getUserInfoId(userId));
        }
        userInfoDORepo.save(EntityConverter.convert(userInfoBO));
    }

    @Override
    public UserInfoBO queryUserInfoByUserId(String userId) {
        return EntityConverter.convert(userInfoDORepo.findByUserId(userId));
    }

    @Override
    public UserInfoBO modifyUserInfoByUserId(String userId, UserInfoBO userInfoBO) {
        UserInfoDO userInfoDO = userInfoDORepo.findByUserId(userId);
        AssertUtil.assertNotNull(userInfoDO, "修改的用户信息不存在");

        UserInfoDO newUserInfoDO = EntityConverter.convert(userInfoBO);
        if (newUserInfoDO.getNickname() != null) {
            userInfoDO.setNickname(newUserInfoDO.getNickname());
        }
        if (newUserInfoDO.getRealName() != null) {
            userInfoDO.setRealName(newUserInfoDO.getRealName());
        }
        if (newUserInfoDO.getSex() != null) {
            userInfoDO.setSex(newUserInfoDO.getSex());
        }
        if (newUserInfoDO.getMobilePhone() != null) {
            userInfoDO.setMobilePhone(newUserInfoDO.getMobilePhone());
        }
        if (newUserInfoDO.getEmail() != null) {
            userInfoDO.setEmail(newUserInfoDO.getEmail());
        }
        if (newUserInfoDO.getBirthday() != null) {
            userInfoDO.setBirthday(newUserInfoDO.getBirthday());
        }
        if (newUserInfoDO.getCompany() != null) {
            userInfoDO.setCompany(newUserInfoDO.getCompany());
        }

        // 拓展信息为 null 时认为是主动清空，只有当拓展信息不为空时才会更新
        if (userInfoBO.getExtInfo() == null) {
            userInfoDO.setExtInfo(JSON.toJSONString(new HashMap<>(0)));
        } else if (!userInfoBO.getExtInfo().isEmpty()) {
            userInfoDO.setExtInfo(newUserInfoDO.getExtInfo());
        }
        return EntityConverter.convert(userInfoDORepo.save(userInfoDO));
    }

    @Override
    public UserInfoBO createUserInfo(UserInfoBO userInfoBO) {
        userInfoBO.setUserInfoId(userBizIdFactory.getUserInfoId(userInfoBO.getUserId()));
        return EntityConverter.convert(userInfoDORepo.save(EntityConverter.convert(userInfoBO)));
    }

}
