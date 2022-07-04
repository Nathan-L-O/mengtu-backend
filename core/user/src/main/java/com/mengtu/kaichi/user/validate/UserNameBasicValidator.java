package com.mengtu.kaichi.user.validate;

import com.mengtu.kaichi.user.dal.service.UserRepoService;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CharsetEncodingUtil;
import com.mengtu.util.validator.Validator;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * 用户名基础校验器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:49
 */
public class UserNameBasicValidator implements Validator<UserManageRequest> {

    @Resource
    private UserRepoService userRepoService;

    @Override
    public boolean support(UserManageRequest user) {
        return true;
    }

    @Override
    public void validate(UserManageRequest request) {
        AssertUtil.assertNotNull(request, "请求不能为空");
        AssertUtil.assertStringNotBlank(request.getUsername(), "用户名不能为空");
        AssertUtil.assertTrue(CharsetEncodingUtil.canEncodeGBK(request.getUsername()) || CharsetEncodingUtil.canEncodeUTF8(request.getUsername()), "用户名不能包含特殊字符");
        UserBO userBO = userRepoService.queryByUserName(request.getUsername());
        AssertUtil.assertNull(userBO, MessageFormat.format("用户名已经注册, {0}", request.getUsername()));
    }
}
