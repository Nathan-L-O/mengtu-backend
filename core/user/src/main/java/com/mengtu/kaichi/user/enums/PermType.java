package com.mengtu.kaichi.user.enums;

/**
 * 权限类型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:35
 */
public interface PermType {

    /**
     * 获取权限类型
     *
     * @return
     */
    String getCode();

    /**
     * 获取权限描述
     *
     * @return
     */
    String getDesc();

    /**
     * 是否需要初始化
     *
     * @return
     */
    boolean isInit();
}
