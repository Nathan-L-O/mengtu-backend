package com.mengtu.util.common;


/**
 * 结果码接口
 *
 * @author 过昊天 @ 2022/4/21 14:16
 * @version 1.0
 */
public interface ResultCode {

    /**
     * 获取码
     *
     * @return
     */
    String getCode();

    /**
     * 获取描述
     *
     * @return
     */
    String getMessage();
}
