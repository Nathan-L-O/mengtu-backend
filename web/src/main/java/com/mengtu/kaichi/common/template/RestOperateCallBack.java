package com.mengtu.kaichi.common.template;

import com.mengtu.util.common.Result;

/**
 * 操作模板
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:56
 */
public interface RestOperateCallBack<T> {

    /**
     * 操作前置
     */
    default void before() {
    }

    /**
     * 执行操作
     *
     * @return
     * @throws Exception
     */
    Result<T> execute() throws Exception;

    /**
     * 操作后置
     */
    default void after() {
    }

}
