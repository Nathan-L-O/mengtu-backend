package com.mengtu.util.validator;

import java.util.List;

/**
 * 复数校验器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:54
 */
@SuppressWarnings("unchecked")
public class MultiValidator<R> {

    /**
     * 校验器组件集合
     */
    private List<Validator> validatorList;

    /**
     * 校验
     *
     * @param request
     */
    public void validate(R request) {
        if (validatorList == null) {
            return;
        }
        for (Validator<R> validator : validatorList) {
            if (validator.support(request)) {
                validator.validate(request);
            }
        }
    }

    public List<Validator> getValidatorList() {
        return validatorList;
    }

    public void setValidatorList(List<Validator> validatorList) {
        this.validatorList = validatorList;
    }
}
