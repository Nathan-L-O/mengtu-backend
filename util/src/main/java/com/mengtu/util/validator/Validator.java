package com.mengtu.util.validator;

/**
 * 校验器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:57
 */
public interface Validator<R> {

    /**
     * 是否支持校验
     *
     * @param request
     * @return
     */
    boolean support(R request);

    /**
     * 校验
     *
     * @param request
     */
    void validate(R request);

}
