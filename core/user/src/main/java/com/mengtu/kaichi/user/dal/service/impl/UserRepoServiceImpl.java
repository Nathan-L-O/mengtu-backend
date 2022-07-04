package com.mengtu.kaichi.user.dal.service.impl;

import com.mengtu.kaichi.user.dal.convert.EntityConverter;
import com.mengtu.kaichi.user.dal.model.perm.UserDO;
import com.mengtu.kaichi.user.dal.repo.perm.UserDORepo;
import com.mengtu.kaichi.user.dal.service.UserRepoService;
import com.mengtu.kaichi.user.idfactory.BizIdFactory;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户仓储服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:35
 */
@Service
public class UserRepoServiceImpl implements UserRepoService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserRepoServiceImpl.class);

    @Resource
    private UserDORepo userDORepo;

    @Resource
    private BizIdFactory userBizIdFactory;

    @Override
    public UserBO queryByUserName(String userName) {
        return EntityConverter.convert(userDORepo.findByUsername(userName));
    }

    @Override
    public UserBO queryByUserId(String userId) {
        return EntityConverter.convert(userDORepo.findByUserId(userId));
    }

    @Override
    public UserBO updateUserByUserId(UserBO userBO) {
        UserDO userDO = userDORepo.findByUserId(userBO.getUserId());
        if (userDO == null) {
            LoggerUtil.error(LOGGER, "更新的用户不存在 UserBO={0}", userBO);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "更新的用户不存在");
        }
        UserDO newUserDO = EntityConverter.convert(userBO);
        if (newUserDO.getUsername() != null) {
            userDO.setUsername(newUserDO.getUsername());
        }
        if (newUserDO.getPassword() != null) {
            userDO.setPassword(newUserDO.getPassword());
        }
        if (newUserDO.getSalt() != null) {
            userDO.setSalt(newUserDO.getSalt());
        }
        if (newUserDO.getLastLoginIP() != null) {
            userDO.setLastLoginIP(newUserDO.getLastLoginIP());
        }
        if (newUserDO.getLastLoginDate() != null) {
            userDO.setLastLoginDate(newUserDO.getLastLoginDate());
        }
        if (newUserDO.getSessionId() != null) {
            userDO.setSessionId(newUserDO.getSessionId());
        }
        return EntityConverter.convert(userDORepo.save(userDO));
    }

    @Override
    public UserBO clearSessionId(String userId) {
        UserDO userDO = userDORepo.findByUserId(userId);
        if (userDO == null) {
            LoggerUtil.error(LOGGER, "用户不存在 userId={0}", userId);
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "用户不存在");
        }
        userDO.setSessionId(null);
        return EntityConverter.convert(userDORepo.save(userDO));
    }

    @Override
    public UserBO queryBySessionId(String sessionId) {
        return EntityConverter.convert(userDORepo.findBySessionId(sessionId));
    }

    @Override
    public UserBO createUser(UserBO userBO) {
        if (StringUtils.isBlank(userBO.getUserId())) {
            userBO.setUserId(userBizIdFactory.getUserId());
        }
        return EntityConverter.convert(userDORepo.save(EntityConverter.convert(userBO)));
    }
}
