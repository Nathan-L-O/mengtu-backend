package com.mengtu.kaichi.common;

import com.mengtu.kaichi.model.user.request.UserRequestBase;
import org.aspectj.lang.JoinPoint;

import javax.servlet.http.HttpServletRequest;

/**
 * rest 切面
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:57
 */
public class RestAop {

    /**
     * 解析 rest 请求
     *
     * @param joinPoint
     */
    protected HttpServletRequest parseHttpServletRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof HttpServletRequest) {
                return (HttpServletRequest) o;
            }
        }
        return null;
    }


    /**
     * 解析 rest 请求
     *
     * @param joinPoint
     */
    protected BaseRestRequest parseRestRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof BaseRestRequest) {
                return (BaseRestRequest) o;
            }
        }
        return null;
    }

    /**
     * 解析 rest 请求
     *
     * @param joinPoint
     */
    protected UserRequestBase parseUserRequest(JoinPoint joinPoint) {
        Object[] objs = joinPoint.getArgs();
        for (Object o : objs) {
            if (o instanceof UserRequestBase) {
                return (UserRequestBase) o;
            }
        }
        return null;
    }
}
