package com.mengtu.kaichi.serviceimpl.user.enums;

import com.mengtu.kaichi.serviceimpl.user.constant.GeneralPermType;
import com.mengtu.kaichi.user.enums.PermType;
import org.apache.commons.lang.StringUtils;

/**
 * GeneralManagerPermTypeEnum
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:20
 */
public enum GeneralManagerPermTypeEnum implements PermType {

    /**
     * 操作权限
     */
    PERM_OPERATOR(GeneralPermType.PERM_OPERATOR, "分配权限", true),
    ;

    private String code;

    private String desc;

    private boolean init;

    public static GeneralManagerPermTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (GeneralManagerPermTypeEnum permType : values()) {
            if (StringUtils.equals(permType.getCode(), code)) {
                return permType;
            }
        }
        return null;
    }

    GeneralManagerPermTypeEnum(String code, String desc, boolean init) {
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
