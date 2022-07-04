package com.mengtu.kaichi.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求限制
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/2 14:16
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Limit {

    int threshold() default 1;

}
