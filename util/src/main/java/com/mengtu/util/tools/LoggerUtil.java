package com.mengtu.util.tools;

import com.mengtu.util.exception.KaiChiException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * 日志工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:53
 */
public class LoggerUtil {

    /**
     * 生成 info 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void info(Logger logger, String template, Object... params) {
        if (logger.isInfoEnabled()) {
            logger.info(getLogString(template, params));
        }
    }

    /**
     * 生成 error 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void error(Logger logger, String template, Object... params) {
        logger.error(getLogString(template, params));
    }

    /**
     * 生成 error 日志，记录异常
     *
     * @param throwable
     * @param logger
     * @param template
     * @param params
     */
    public static void error(Throwable throwable, Logger logger, String template, Object... params) {
        String errorCode = null;
        String errorMsg;
        if (throwable instanceof Exception) {
            Exception exception = (Exception) throwable;
            errorMsg = exception.getMessage();
        } else {
            errorCode = getErrorCode(throwable);
            errorMsg = errorCode;
        }
        logger.error(getLogString(template, params) + ", ErrorCode=" + errorCode + ", errorMsg=" + errorMsg, throwable);
    }

    /**
     * 生成 warn 日志
     *
     * @param logger
     * @param template
     * @param params
     */
    public static void warn(Logger logger, String template, Object... params) {
        logger.warn(getLogString(template, params));
    }

    /**
     * 生成 warn 日志，记录异常
     *
     * @param throwable
     * @param logger
     * @param template
     * @param params
     */
    public static void warn(Throwable throwable, Logger logger, String template, Object... params) {
        String errorCode;
        String errorMsg;
        if (throwable instanceof KaiChiException) {
            KaiChiException kaiChiException = (KaiChiException) throwable;
            errorCode = kaiChiException.getErrorCode();
            errorMsg = kaiChiException.getMessage();
        } else {
            errorCode = getErrorCode(throwable);
            errorMsg = errorCode;
        }
        logger.warn(getLogString(template, params) + ", ErrorCode=" + errorCode + ", errorMsg=" + errorMsg, throwable);
    }

    /**
     * 生成日志
     *
     * @param template
     * @param params
     * @return
     */
    private static String getLogString(String template, Object... params) {
        return MessageFormat.format(template, params);
    }

    /**
     * 获取异常 code
     *
     * @param throwable
     * @return
     */
    private static String getErrorCode(Throwable throwable) {
        if (throwable == null) {
            return StringUtils.EMPTY;
        }
        try {
            Method mt = throwable.getClass().getDeclaredMethod("getCode");
            if (mt != null) {
                Object obj = mt.invoke(throwable);
                if (obj != null) {
                    obj.toString();
                }
            }
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }

}
