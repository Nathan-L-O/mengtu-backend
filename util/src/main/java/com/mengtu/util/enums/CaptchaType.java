package com.mengtu.util.enums;

import com.mengtu.util.common.ResultCode;

/**
 * Captcha 类型
 *
 * @author 过昊天 @ 2022/4/21 14:19
 * @version 1.0
 */
public enum CaptchaType implements ResultCode {

    /**
     * NORMAL
     */
    NORMAL("NORMAL", "普通验证码"),

    /**
     * 数字验证码
     */
    NUMBER("NUMBER", "数字验证码"),

    /**
     * EXPRESSION
     */
    EXPRESSION("EXPRESSION", "表达式"),
    ;

    private final String code;

    private final String message;

    CaptchaType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CaptchaType getByCode(String code) {
        for (CaptchaType restResultCode : values()) {
            if (org.apache.commons.lang.StringUtils.equals(restResultCode.getCode(), code)) {
                return restResultCode;
            }
        }
        return null;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
