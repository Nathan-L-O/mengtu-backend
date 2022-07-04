package com.mengtu.kaichi.linkpoint.idfactory;

/**
 * linkpoint Id 类型枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/31 10:22
 */
public enum IdTypeEnum {

    /**
     * 版本 id
     */
    PROJECT_ID("1001", "linkpoint 项目 ID"),

    /**
     * 版本历史id
     */
    PROJECT_VERSION_ID("1002", "linkpoint 项目历史 ID"),

    /**
     * 模型 ID
     */
    VAULT_MODEL_ID("1003", "linkpoint 模型 ID");

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
