package com.mengtu.util.exception;

import com.mengtu.util.common.ResultCode;
import com.mengtu.util.enums.CommonResultCode;

/**
 * KaiChi 通用业务异常
 *
 * @author 过昊天 @ 2022/4/21 14:19
 * @version 1.0
 */
public class KaiChiException extends RuntimeException {

    private static final long serialVersionUID = -4559736423824194476L;

    /**
     * 异常码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 构造器
     */
    public KaiChiException(final Throwable cause) {
        super(cause);
        this.errorCode = CommonResultCode.SYSTEM_ERROR.getCode();
        this.message = CommonResultCode.SYSTEM_ERROR.getMessage();
    }

    public KaiChiException(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public KaiChiException(ResultCode resultCode) {
        this.errorCode = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public KaiChiException(ResultCode resultCode, String message) {
        this.errorCode = resultCode.getCode();
        this.message = message;
    }

    public KaiChiException(Throwable cause, String errorCode, String message) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public KaiChiException(String message) {
        super(message);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 异常获取信息，重载
     */
    @Override
    public String getMessage() {
        return message;
    }
}
