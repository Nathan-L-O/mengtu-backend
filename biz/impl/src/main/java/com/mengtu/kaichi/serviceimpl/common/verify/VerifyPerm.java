package com.mengtu.kaichi.serviceimpl.common.verify;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 鉴权切面
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyPerm {
    String[] permType();
}
