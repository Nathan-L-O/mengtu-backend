package com.mengtu.util.verification.model;

import com.mengtu.util.enums.CaptchaType;
import com.mengtu.util.verification.VerificationCodeUtil;

import java.util.List;

/**
 * 用户验证码模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:55
 */
public class UserVerificationCode {

    /**
     * UserID
     */
    private final String userId;

    /**
     * 验证码
     */
    private final String code;

    /**
     * 验证码校验体
     */
    private final String validateCode;

    /**
     * 时间戳
     */
    private final long timestamp;

    public UserVerificationCode(String userId, CaptchaType captchaType) {
        this.userId = userId;
        List<String> codeModel;
        if (captchaType.equals(CaptchaType.NORMAL)) {
            codeModel = VerificationCodeUtil.getCode();
            this.code = codeModel.get(0);
            this.validateCode = codeModel.get(0);
        } else if (captchaType.equals(CaptchaType.NUMBER)) {
            codeModel = VerificationCodeUtil.getNumCode();
            this.code = codeModel.get(0);
            this.validateCode = codeModel.get(0);
        } else {
            codeModel = VerificationCodeUtil.getExpression();
            this.code = codeModel.get(0);
            this.validateCode = codeModel.get(1);
        }
        this.timestamp = System.currentTimeMillis() / 1000;
    }

    public String getUserId() {
        return userId;
    }

    public String getCode() {
        return code;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
