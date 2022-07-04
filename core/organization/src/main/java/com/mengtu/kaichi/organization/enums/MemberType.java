package com.mengtu.kaichi.organization.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 成员类型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:43
 */
public enum MemberType {
    /**
     * 负责人
     */
    PRINCIPAL("PRINCIPAL", "负责人"),

    /**
     * 管理员
     */
    ADMIN("ADMIN", "管理员"),

    /**
     * 成员
     */
    MEMBER("MEMBER", "成员"),

    /**
     * 社长
     */
    ASSOCIATION_LEADER("ASSOCIATION_LEADER", "社长");


    public static MemberType getByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (MemberType memberType : values()) {
                if (StringUtils.equals(memberType.getType(), type)) {
                    return memberType;
                }
            }
        }
        return null;
    }

    private String type;

    private String desc;

    MemberType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}