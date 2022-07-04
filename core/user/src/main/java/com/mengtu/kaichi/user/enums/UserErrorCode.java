package com.mengtu.kaichi.user.enums;

import com.mengtu.util.common.ResultCode;
import com.mengtu.util.enums.CommonResultCode;

/**
 * 用户异常码
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/25 11:05
 */
public enum UserErrorCode implements ResultCode {

    /**
     * 用户名或密码错误
     */
    INCORRECT_USERNAME_OR_PASSWORD(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "用户名或密码错误"),

    /**
     * 用户不存在
     */
    USER_NOT_EXIST(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "用户不存在"),

    /**
     * 用户未登录
     */
    USER_NOT_LOGIN(CommonResultCode.UNAUTHORIZED.getCode(), "用户未登录"),

    /**
     * 用户名不能为空
     */
    INVALID_USERNAME(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "用户名不能为空"),

    /**
     * 用户名不能为空
     */
    INVALID_PASSWORD(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "密码不能为空");

    private final String code;

    private final String message;

    UserErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
