package com.mengtu.util.log;

import org.apache.commons.lang.StringUtils;

/**
 * 日志级别枚举
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:52
 */
public enum LogLevel {

    /**
     * Trace
     */
    TRACE,

    /**
     * Debug
     */
    DEBUG,

    /**
     * Info Log
     */
    INFO,

    /**
     * Warn
     */
    WARN,

    /**
     * Error
     */
    ERROR;

    public static LogLevel getByCode(String level) {
        if (StringUtils.isBlank(level)) {
            return null;
        }
        for (LogLevel logLevel : values()) {
            if (StringUtils.equals(level, logLevel.name())) {
                return logLevel;
            }
        }
        return null;
    }

}
