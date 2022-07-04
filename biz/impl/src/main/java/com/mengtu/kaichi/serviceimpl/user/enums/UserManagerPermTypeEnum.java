package com.mengtu.kaichi.serviceimpl.user.enums;

import com.mengtu.kaichi.serviceimpl.user.constant.UserPermType;
import com.mengtu.kaichi.user.enums.PermType;
import org.apache.commons.lang.StringUtils;

/**
 * UserManagerPermTypeEnum
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:20
 */
public enum UserManagerPermTypeEnum implements PermType {

    /**
     * 用户密码重置
     */
    USER_PASSWORD_RESET(UserPermType.USER_PASSWORD_RESET, "重置用户密码", true),
    ;

    private String code;

    private String desc;

    private boolean init;

    public static UserManagerPermTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (UserManagerPermTypeEnum permType : values()) {
            if (StringUtils.equals(permType.getCode(), code)) {
                return permType;
            }
        }
        return null;
    }

    UserManagerPermTypeEnum(String code, String desc, boolean init) {
        this.code = code;
        this.desc = desc;
        this.init = init;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public boolean isInit() {
        return init;
    }
}
