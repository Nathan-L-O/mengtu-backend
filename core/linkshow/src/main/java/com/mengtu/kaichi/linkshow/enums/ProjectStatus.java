package com.mengtu.kaichi.linkshow.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 状态类型枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:43
 */
public enum ProjectStatus {
    /**
     * 进行中
     */
    UNDERWAY("UNDERWAY", 0),

    /**
     * 评审中
     */
    UNDER_REVIEW("UNDER_REVIEW", 1),

    /**
     * 已完成
     */
    COMPLETED("COMPLETED", 2);


    public static ProjectStatus getByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (ProjectStatus projectStatus : values()) {
                if (StringUtils.equals(projectStatus.getType(), type)) {
                    return projectStatus;
                }
            }
        }
        return null;
    }

    private String type;

    private int desc;

    ProjectStatus(String type, int desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public int getDesc() {
        return desc;
    }
}