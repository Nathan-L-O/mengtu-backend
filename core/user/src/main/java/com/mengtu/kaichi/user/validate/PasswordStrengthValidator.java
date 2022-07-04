package com.mengtu.kaichi.user.validate;

import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.validator.Validator;

/**
 * 密码校验器（强度）
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:46
 */
public class PasswordStrengthValidator implements Validator<UserManageRequest> {

    /**
     * 密码强度最低标准规约
     */
    private final static int PASSWORD_STRENGTH = 2;

    @Override
    public boolean support(UserManageRequest request) {
        return true;
    }

    @Override
    public void validate(UserManageRequest request) {
        AssertUtil.assertNotNull(request, "请求不能为空");
        AssertUtil.assertStringNotBlank(request.getPassword(), "密码不能为空");
        AssertUtil.assertTrue(calculateStrength(request.getPassword()) >= PASSWORD_STRENGTH, "密码强度过弱");
    }

    /**
     * 密码强度计算
     *
     * @param password
     * @return
     */
    private int calculateStrength(String password) {
        boolean containNum = false;
        boolean containUppercase = false;
        boolean containLowercase = false;
        boolean containSymbol = false;

        for (char s : password.toCharArray()) {
            if (48 <= s && 57 >= s) {
                containNum = true;
                continue;
            }
            if (65 <= s && 90 >= s) {
                containUppercase = true;
                continue;
            }
            if (97 <= s && 122 >= s) {
                containLowercase = true;
                continue;
            }
            containSymbol = true;
        }

        return (containNum ? 1 : 0) + (containUppercase ? 1 : 0) + (containLowercase ? 1 : 0) + (containSymbol ? 1 : 0);
    }
}
