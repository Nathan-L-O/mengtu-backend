package com.mengtu.kaichi.converter.user;

import com.mengtu.kaichi.model.user.vo.UserVO;
import com.mengtu.kaichi.user.model.CommonUser;
import com.mengtu.kaichi.user.model.basic.perm.RoleBO;
import com.mengtu.util.tools.CollectionUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * 用户页面模型转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:58
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class UserVOConverter {

    /**
     * 用户业务模型转前端模型
     *
     * @param commonUser
     * @return
     */
    public static UserVO convert(CommonUser commonUser) {
        if (commonUser == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        userVO.setToken(commonUser.getToken());
        userVO.setUserId(commonUser.getUserId());
        userVO.setUserInfo(commonUser.getUserInfo());
        userVO.setRoleInfo(new ArrayList<>(CollectionUtil.toStream(commonUser.getRoleInfo().values()).map(RoleBO::getRoleCode).collect(Collectors.toSet())));
        return userVO;
    }
}
