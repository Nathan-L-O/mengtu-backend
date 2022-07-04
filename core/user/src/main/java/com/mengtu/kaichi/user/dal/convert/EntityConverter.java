package com.mengtu.kaichi.user.dal.convert;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import com.mengtu.kaichi.user.dal.model.perm.PermDO;
import com.mengtu.kaichi.user.dal.model.perm.RoleDO;
import com.mengtu.kaichi.user.dal.model.perm.UserDO;
import com.mengtu.kaichi.user.dal.model.perm.UserRoleRelationDO;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.model.basic.perm.UserRoleRelationBO;

import java.util.Map;

/**
 * 实体转换器
 *
 * @author 过昊天
 * @version 1.1 @ 2022/4/25 09:59
 */
final public class EntityConverter {

    /**
     * 用户 DO -> BO
     *
     * @param userDO
     * @return
     */
    public static UserBO convert(UserDO userDO) {
        if (userDO == null) {
            return null;
        }
        UserBO userBO = new UserBO();
        userBO.setUserId(userDO.getUserId());
        userBO.setUserName(userDO.getUsername());
        userBO.setPassword(userDO.getPassword());
        userBO.setSalt(userDO.getSalt());
        userBO.setLastLoginIp(userDO.getLastLoginIP());
        userBO.setLastLoginDate(userDO.getLastLoginDate());
        userBO.setSessionId(userDO.getSessionId());
        return userBO;
    }

    /**
     * 用户 BO -> DO
     *
     * @param userBO
     * @return
     */
    public static UserDO convert(UserBO userBO) {
        if (userBO == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        userDO.setUserId(userBO.getUserId());
        userDO.setUsername(userBO.getUserName());
        userDO.setPassword(userBO.getPassword());
        userDO.setSalt(userBO.getSalt());
        userDO.setLastLoginIP(userBO.getLastLoginIp());
        userDO.setLastLoginDate(userBO.getLastLoginDate());
        userDO.setSessionId(userBO.getSessionId());
        return userDO;
    }

    /**
     * 用户信息 DO -> BO
     *
     * @param userInfoDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static UserInfoBO convert(UserInfoDO userInfoDO) {
        if (userInfoDO == null) {
            return null;
        }
        UserInfoBO userInfoBO = new UserInfoBO();
        userInfoBO.setUserInfoId(userInfoDO.getUserInfoId());
        userInfoBO.setUserId(userInfoDO.getUserId());
        userInfoBO.setRegistrationTime(userInfoDO.getGmtCreate());
        userInfoBO.setRealName(userInfoDO.getRealName());
        userInfoBO.setSex(userInfoDO.getSex());
        userInfoBO.setMobilePhone(userInfoDO.getMobilePhone());
        userInfoBO.setEmail(userInfoDO.getEmail());
        userInfoBO.setCompany(userInfoDO.getCompany());
        userInfoBO.setBirthday(userInfoDO.getBirthday());
        userInfoBO.setNickname(userInfoDO.getNickname());
        userInfoBO.setRealNameCheck(userInfoDO.getRealNameCheck());
        userInfoBO.setExtInfo(JSON.parseObject(userInfoDO.getExtInfo(), Map.class));
        return userInfoBO;
    }

    /**
     * 用户信息 BO -> DO
     *
     * @param userInfoBO
     * @return
     */
    public static UserInfoDO convert(UserInfoBO userInfoBO) {
        if (userInfoBO == null) {
            return null;
        }
        UserInfoDO userInfoDO = new UserInfoDO();
        userInfoDO.setUserInfoId(userInfoBO.getUserInfoId());
        userInfoDO.setUserId(userInfoBO.getUserId());
        userInfoDO.setRealName(userInfoBO.getRealName());
        userInfoDO.setSex(userInfoBO.getSex());
        userInfoDO.setMobilePhone(userInfoBO.getMobilePhone());
        userInfoDO.setEmail(userInfoBO.getEmail());
        userInfoDO.setCompany(userInfoBO.getCompany());
        userInfoDO.setBirthday(userInfoBO.getBirthday());
        userInfoDO.setNickname(userInfoBO.getNickname());
        userInfoDO.setRealNameCheck(userInfoBO.getRealNameCheck());
        userInfoDO.setExtInfo(JSON.toJSONString(userInfoBO.getExtInfo()));
        return userInfoDO;
    }

    /**
     * 权限 DO -> BO
     *
     * @param permDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static PermBO convert(PermDO permDO) {
        if (permDO == null) {
            return null;
        }
        PermBO permBO = new PermBO();
        permBO.setPermId(permDO.getPermId());
        permBO.setPermType(permDO.getPermType());
        permBO.setPermName(permDO.getPermName());
        permBO.setPermDesc(permDO.getPermDesc());
        permBO.setExtInfo(JSON.parseObject(permDO.getExtInfo(), Map.class));
        return permBO;
    }

    /**
     * 权限 BO -> DO
     *
     * @param permBO
     * @return
     */
    public static PermDO convert(PermBO permBO) {
        if (permBO == null) {
            return null;
        }
        PermDO permDO = new PermDO();
        permDO.setPermId(permBO.getPermId());
        permDO.setPermType(permBO.getPermType());
        permDO.setPermName(permBO.getPermName());
        permDO.setPermDesc(permBO.getPermDesc());
        permDO.setExtInfo(JSON.toJSONString(permBO.getExtInfo()));
        return permDO;
    }

    /**
     * 角色 DO -> BO
     *
     * @param roleDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static RoleBO convert(RoleDO roleDO) {
        if (roleDO == null) {
            return null;
        }
        RoleBO roleBO = new RoleBO();
        roleBO.setRoleCode(roleDO.getRoleCode());
        roleBO.setRoleName(roleDO.getRoleName());
        roleBO.setRoleId(roleDO.getRoleId());
        roleBO.setRoleDesc(roleDO.getRoleDesc());
        roleBO.setExtInfo(JSON.parseObject(roleDO.getExtInfo(), Map.class));
        return roleBO;
    }

    /**
     * 角色 BO -> DO
     *
     * @param roleBO
     * @return
     */
    public static RoleDO convert(RoleBO roleBO) {
        if (roleBO == null) {
            return null;
        }
        RoleDO roleDO = new RoleDO();
        roleDO.setRoleCode(roleBO.getRoleCode());
        roleDO.setRoleName(roleBO.getRoleName());
        roleDO.setRoleId(roleBO.getRoleId());
        roleDO.setRoleDesc(roleBO.getRoleDesc());
        roleDO.setExtInfo(JSON.toJSONString(roleBO.getExtInfo()));
        return roleDO;
    }

    /**
     * 用户角色关联 DO -> BO
     *
     * @param userRoleRelationDO
     * @return
     */
    public static UserRoleRelationBO convert(UserRoleRelationDO userRoleRelationDO) {
        if (userRoleRelationDO == null) {
            return null;
        }
        UserRoleRelationBO userRoleRelationBO = new UserRoleRelationBO();
        userRoleRelationBO.setRoleId(userRoleRelationDO.getRoleId());
        userRoleRelationBO.setUserId(userRoleRelationDO.getUserId());
        userRoleRelationBO.setUserRoleId(userRoleRelationDO.getUserRoleId());
        return userRoleRelationBO;
    }


}
