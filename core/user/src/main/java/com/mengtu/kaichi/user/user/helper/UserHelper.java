package com.mengtu.kaichi.user.user.helper;

import com.mengtu.kaichi.user.dal.service.RoleRepoService;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.model.BasicUser;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.tools.AssertUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


/**
 * 用户工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:45
 */
@Component
public class UserHelper {

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private RoleRepoService roleRepoService;

    /**
     * 用户模型校验
     *
     * @param user
     */
    protected void checkBaseUser(BasicUser user) {
        AssertUtil.assertNotNull(user, CommonResultCode.SYSTEM_ERROR, "用户不能为空");
        AssertUtil.assertStringNotBlank(user.getUserId(), CommonResultCode.SYSTEM_ERROR, "用户id不能为空");
    }

    /**
     * 填充用户信息
     *
     * @param user
     */
    public void fillUserInfo(BasicUser user) {
        checkBaseUser(user);
        user.setUserInfo(userInfoRepoService.queryUserInfoByUserId(user.getUserId()));

    }

    /**
     * 用户的角色
     *
     * @param userId
     */
    public Map<String, RoleBO> fetchUserRoles(String userId) {
        AssertUtil.assertStringNotBlank(userId, CommonResultCode.SYSTEM_ERROR, "用户id不能为空");

        Map<String, RoleBO> roles = new HashMap<>();

        // 组装用户上的角色
        List<RoleBO> roleBOS = roleRepoService.queryRolesByUserId(userId);
        putMap(roles, roleBOS, RoleBO::getRoleId);

        return roles;
    }

    /**
     * 存入map
     *
     * @param map
     * @param objList
     */
    private <T> void putMap(Map<String, T> map, List<T> objList, Function<T, String> getKey) {
        AssertUtil.assertNotNull(objList);
        if (map == null) {
            map = new HashMap<>();
        }
        for (T t : objList) {
            if (t != null) {
                map.put(getKey.apply(t), t);
            }
        }
    }

}
