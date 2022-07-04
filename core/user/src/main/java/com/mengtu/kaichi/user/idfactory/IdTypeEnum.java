package com.mengtu.kaichi.user.idfactory;

/**
 * 业务 ID 类型枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:37
 */
public enum IdTypeEnum {

    /**
     * 用户 ID
     */
    USER_ID("0001", "用户 ID"),

    /**
     * 用户信息id
     */
    USER_INFO_ID("0002", "用户信息 ID"),

    /**
     * 角色 ID
     */
    ROLE_ID("0003", "角色 ID"),

    /**
     * 用户角色关联 ID
     */
    USER_ROLE_RELATION_ID("0004", "用户角色关联 ID"),

    /**
     * 权限id
     */
    PERM_ID("0005", "权限 ID"),

    /**
     * 角色权限关联id
     */
    ROLE_PERM_RELATION_ID("0006", "角色权限关联 ID"),

    /**
     * 用户权限关联id
     */
    USER_PERM_RELATION_ID("0007", "用户权限关联 ID");

    /**
     * 业务 ID
     */
    private final String bizId;

    /**
     * 描述
     */
    private final String desc;

    IdTypeEnum(String bizId, String desc) {
        this.bizId = bizId;
        this.desc = desc;
    }

    public String getBizId() {
        return bizId;
    }

    public String getDesc() {
        return desc;
    }

}
