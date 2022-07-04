package com.mengtu.kaichi.organization.validate;

import com.mengtu.kaichi.organization.request.OrganizationManageRequest;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CharsetEncodingUtil;
import com.mengtu.util.validator.Validator;

/**
 * 组织名基础校验器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/6 13:58
 */
public class OrganizationNameBasicValidator implements Validator<OrganizationManageRequest> {

    @Override
    public boolean support(OrganizationManageRequest organizationManageRequest) {
        return true;
    }

    @Override
    public void validate(OrganizationManageRequest request) {
        AssertUtil.assertNotNull(request, "请求不能为空");
        AssertUtil.assertStringNotBlank(request.getOrganizationName(), "组织名不能为空");
        AssertUtil.assertTrue(CharsetEncodingUtil.canEncodeGBK(request.getOrganizationName()) || CharsetEncodingUtil.canEncodeUTF8(request.getOrganizationName()), "组织名不能包含特殊字符");
    }
}
