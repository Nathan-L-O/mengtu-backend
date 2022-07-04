package com.mengtu.util.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用户日志
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:52
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {

    /**
     * 日志名称
     *
     * @return
     */
    String loggerName();

    /**
     * 日志标识
     *
     * @return
     */
    String identity() default LogMark.DEFAULT;

    /**
     * 日志级别
     *
     * @return
     */
    LogLevel logLevel() default LogLevel.INFO;
}
