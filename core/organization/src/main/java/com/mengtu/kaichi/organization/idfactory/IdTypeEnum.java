package com.mengtu.kaichi.organization.idfactory;

/**
 * Id 类型枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:39
 */
public enum IdTypeEnum {

    /**
     * 组织id
     */
    ORGANIZATION_ID("0011", "组织id"),

    /**
     * 组织成员关系id
     */
    ORGANIZATION_MEMBER_ID("0012", "组织成员关系id"),

    /**
     * 组织关系id
     */
    ORGANIZATION_RELATION_ID("0013", "组织关系id"),

    /**
     * 组织邀请id
     */
    ORGANIZATION_INVITATION_ID("0014", "组织邀请id");

    /**
     * 业务id
     */
    private final String bizNum;

    /**
     * 描述
     */
    private final String desc;

    IdTypeEnum(String bizNum, String desc) {
        this.bizNum = bizNum;
        this.desc = desc;
    }

    public String getBizNum() {
        return bizNum;
    }

    public String getDesc() {
        return desc;
    }
}
