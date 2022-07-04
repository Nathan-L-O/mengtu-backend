package com.mengtu.util.tools;

import com.mengtu.util.common.ResultCode;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import org.apache.commons.lang.StringUtils;

/**
 * 断言工具类
 *
 * @author 过昊天
 * @version 1.3 @ 2022/5/25 11:06
 */
public class AssertUtil {

    /**
     * 断言真表达式
     *
     * @param expression
     */
    public static void assertTrue(Boolean expression) {
        if (expression == null || !expression) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertTrue(Boolean expression, String errorMsg) {
        if (expression == null || !expression) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode) {
        if (expression == null || !expression) {
            throw new KaiChiException(resultCode);
        }
    }

    public static void assertTrue(Boolean expression, ResultCode resultCode, String errorMsg) {
        if (expression == null || !expression) {
            throw new KaiChiException(resultCode, errorMsg);
        }
    }

    /**
     * 断言空对象
     *
     * @param obj
     * @param errorMsg
     */
    public static void assertNull(Object obj, String errorMsg) {
        if (obj != null) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    /**
     * 断言非空对象
     *
     * @param obj
     */
    public static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertNotNull(Object obj, String errorMsg) {
        assertNotNull(obj, CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
    }

    public static void assertNotNull(Object obj, ResultCode resultCode, String errorMsg) {
        if (obj == null) {
            throw new KaiChiException(resultCode, errorMsg);
        }
    }

    public static void assertNotNull(Object obj, ResultCode resultCode) {
        if (obj == null) {
            throw new KaiChiException(resultCode);
        }
    }

    /**
     * 断言非空字符串
     *
     * @param str
     * @param errorMsg
     */
    public static void assertStringNotBlank(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertStringNotBlank(String str, ResultCode resultCode, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new KaiChiException(resultCode, errorMsg);
        }
    }

    public static void assertStringNotBlank(String str, ResultCode resultCode) {
        if (StringUtils.isBlank(str)) {
            throw new KaiChiException(resultCode);
        }
    }

    public static void assertStringNotBlank(String str) {
        if (StringUtils.isBlank(str)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    public static void assertStringBlank(String str, String errorMsg) {
        if (StringUtils.isNotBlank(str)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    /**
     * 断言对象相等
     *
     * @param o1
     * @param o2
     * @param errorMsg
     */
    public static void assertEquals(Object o1, Object o2, String errorMsg) {
        if (!o1.equals(o2)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertEquals(Object o1, Object o2, ResultCode resultCode) {
        if (!o1.equals(o2)) {
            throw new KaiChiException(resultCode);
        }
    }

    public static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

    /**
     * 断言对象不等
     *
     * @param o1
     * @param o2
     * @param errorMsg
     */
    public static void assertNotEquals(Object o1, Object o2, String errorMsg) {
        if (o1.equals(o2)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, errorMsg);
        }
    }

    public static void assertNotEquals(Object o1, Object o2) {
        if (o1.equals(o2)) {
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS);
        }
    }

}
