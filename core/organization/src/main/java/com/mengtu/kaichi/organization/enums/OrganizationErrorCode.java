package com.mengtu.kaichi.organization.enums;

import com.mengtu.util.common.ResultCode;
import com.mengtu.util.enums.CommonResultCode;

/**
 * 组织异常码
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/25 10:37
 */
public enum OrganizationErrorCode implements ResultCode {

    /**
     * 组织不存在
     */
    ORGANIZATION_NOT_EXIST(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "组织不存在"),

    /**
     * 组织名不能为空
     */
    INVALID_ORGANIZATION_NAME(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "组织名不能为空"),

    /**
     * 组织 ID 不能为空
     */
    INVALID_ORGANIZATION_ID(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "组织 ID 不能为空"),

    /**
     * 组织描述不能为空
     */
    INVALID_ORGANIZATION_DESCRIPTION(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), "组织描述不能为空"),
    ;

    private final String code;

    private final String message;

    OrganizationErrorCode(String code, String message) {
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
