package com.mengtu.kaichi.linkshow.idfactory;

/**
 * linkshow Id 类型枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:13
 */
public enum IdTypeEnum {

    /**
     * 版本 id
     */
    PROJECT_ID("1011", "linkshow 项目 ID"),

    /**
     * 版本历史id
     */
    PROJECT_VERSION_ID("1012", "linkshow 项目历史 ID");

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
