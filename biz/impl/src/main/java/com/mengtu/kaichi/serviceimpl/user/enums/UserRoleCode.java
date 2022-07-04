package com.mengtu.kaichi.serviceimpl.user.enums;

import com.mengtu.kaichi.user.enums.RoleCode;
import org.apache.commons.lang.StringUtils;

/**
 * 用户权限码
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 13:21
 */
public enum UserRoleCode implements RoleCode {

    /**
     * 活动管理者 管理活动
     */
    ACTIVITY_MANAGER("ACTIVITY_MANAGER", "活动管理员"),

    /**
     * 活动盖章员 盖章入口
     */
    ACTIVITY_STAMPER("ACTIVITY_STAMPER", "活动盖章员"),

    /**
     * 物资管理者 管理物资
     */
    ASSET_MANAGER("ASSET_MANAGER", "物资管理员"),

    /**
     * 组织管理员 管理所有组织相关
     */
    ORGANIZATION_MANAGER("ORGANIZATION_MANAGER", "组织管理员"),

    /**
     * 证书审核员
     */
    CERTIFICATE_CONFIRM("CERTIFICATE_CONFIRM", "证书审核员"),

    /**
     * 证书管理员
     */
    CERTIFICATE_MANAGER("CERTIFICATE_MANAGER", "证书管理员"),

    /**
     * 场地申请人
     */
    LOCALE_MEMBER("LOCALE_MEMBER", "场地申请人"),

    /**
     * 用户信息管理员
     */
    USER_MANAGER("USER_MANAGER", "用户信息管理员"),

    /**
     * 管理活动管理员的导章等权限
     */
    GENERAL_MANAGER("GENERAL_MANAGER", "总管理"),
    ;

    public static UserRoleCode getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (UserRoleCode roleCode : values()) {
            if (StringUtils.equals(roleCode.getCode(), code)) {
                return roleCode;
            }
        }
        return null;
    }

    private String code;

    private String desc;


    UserRoleCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }


}
