package com.mengtu.kaichi.linkpoint.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 归档状态枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:07
 */
public enum ArchiveStatus {
    /**
     * 正常
     */
    NORMAL("NORMAL", 0),

    /**
     * 归档
     */
    ARCHIVED("ARCHIVED", 1);


    public static ArchiveStatus getByType(String type) {
        if (StringUtils.isNotBlank(type)) {
            for (ArchiveStatus archiveStatus : values()) {
                if (StringUtils.equals(archiveStatus.getType(), type)) {
                    return archiveStatus;
                }
            }
        }
        return null;
    }

    private String type;

    private int desc;

    ArchiveStatus(String type, int desc) {
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