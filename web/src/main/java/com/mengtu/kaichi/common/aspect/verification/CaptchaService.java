package com.mengtu.kaichi.common.aspect.verification;

import com.mengtu.kaichi.common.RestAop;
import com.mengtu.kaichi.model.user.request.UserRequestBase;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.verification.VerificationCodeUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Rest 切面人机验证服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:56
 */
@Order(1)
@Aspect
@Component
public class CaptchaService extends RestAop {

    @Pointcut("execution(* com.mengtu.kaichi.controller..*(..)) && @annotation(com.mengtu.kaichi.common.annotation.Captcha)")
    public void captcha() {
    }

    /**
     * 验证码状态校验
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "captcha()")
    public Object verifyCaptcha(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        UserRequestBase request = parseUserRequest(proceedingJoinPoint);
        AssertUtil.assertNotNull(request, CommonResultCode.SYSTEM_ERROR, "登陆请求缺失");
        AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");

        if (!VerificationCodeUtil.validate(request.getUsername())) {
            return RestResultUtil.buildResult(RestResultCode.UNAUTHORIZED, "未通过人机测试");
        }

        return proceedingJoinPoint.proceed();
    }

    /**
     * 验证码状态复位
     *
     * @param joinPoint
     * @param result
     */
    @AfterReturning(value = "captcha()", returning = "result")
    public void resetCaptcha(JoinPoint joinPoint, Object result) {
        try {
            if (RestResultCode.SUCCESS.getCode().equals(((Result) result).getErrorCode())) {
                VerificationCodeUtil.afterCaptcha(parseUserRequest(joinPoint).getUsername());
            }
        } catch (Exception ignored) {
        }
    }

}
