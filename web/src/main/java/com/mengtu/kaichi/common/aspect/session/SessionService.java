package com.mengtu.kaichi.common.aspect.session;

import com.mengtu.kaichi.common.BaseRestRequest;
import com.mengtu.kaichi.common.RestAop;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.kaichi.util.IPUtil;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Rest 切面会话服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:56
 */
@Order(1)
@Aspect
@Component
public class SessionService extends RestAop {

    /**
     * HTTP 请求头 token
     */
    private static final String TOKEN = "Authorization";

    @Resource
    private UserBasicService userBasicService;

    @Pointcut("execution(* com.mengtu.kaichi.controller..*(..)) && @annotation(com.mengtu.kaichi.common.annotation.ValidateToken)")
    public void token() {
    }

    /**
     * 登陆态校验
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "token()")
    public Object verifyToken(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        BaseRestRequest request = parseRestRequest(proceedingJoinPoint);
        HttpServletRequest httpServletRequest = parseHttpServletRequest(proceedingJoinPoint);
        AssertUtil.assertNotNull(request, CommonResultCode.SYSTEM_ERROR, "登陆请求缺失");
        AssertUtil.assertNotNull(httpServletRequest, CommonResultCode.SYSTEM_ERROR, "登陆凭证缺失");

        String token = httpServletRequest.getHeader(TOKEN);
        if (StringUtils.isBlank(token)) {
            return RestResultUtil.buildResult(RestResultCode.UNAUTHORIZED, "登陆凭证缺失");
        }
        System.out.println(token);
        UserBO userBO = userBasicService.checkLogin(token, IPUtil.getIp(httpServletRequest));
        if (userBO == null) {
            return RestResultUtil.buildResult(RestResultCode.UNAUTHORIZED, "用户未登录");
        }
        request.setUserId(userBO.getUserId());

        return proceedingJoinPoint.proceed();
    }

}
