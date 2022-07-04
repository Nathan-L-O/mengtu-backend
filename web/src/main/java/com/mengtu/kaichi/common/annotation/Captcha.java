package com.mengtu.kaichi.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 图形验证码校验
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Captcha {

}
