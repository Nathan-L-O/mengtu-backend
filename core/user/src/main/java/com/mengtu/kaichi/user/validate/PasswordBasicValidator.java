package com.mengtu.kaichi.user.validate;

import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.validator.Validator;

/**
 * 密码校验器（基础规则）
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:46
 */
public class PasswordBasicValidator implements Validator<UserManageRequest> {

    /**
     * 密码最小长度规约
     */
    private final static int PASSWORD_MIN_LENGTH = 6;

    /**
     * 密码最大长度规约
     */
    private final static int PASSWORD_MAX_LENGTH = 32;

    @Override
    public boolean support(UserManageRequest request) {
        return true;
    }

    @Override
    public void validate(UserManageRequest request) {
        AssertUtil.assertNotNull(request, "请求不能为空");
        AssertUtil.assertStringNotBlank(request.getPassword(), "密码不能为空");
        AssertUtil.assertTrue(request.getPassword().length() <= PASSWORD_MAX_LENGTH, "密码长度不能超过32位");
        AssertUtil.assertTrue(request.getPassword().length() >= PASSWORD_MIN_LENGTH, "密码长度不能少于6位");
    }
}
