package com.mengtu.kaichi.serviceimpl.common.verify;

import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * 鉴权切面服务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:44
 */
@Aspect
@Component
public class VerifyPermService {

    private final Logger LOGGER = LoggerFactory.getLogger(VerifyPermService.class);

    @Resource
    private UserBasicService userBasicService;

    @Pointcut("within(com.mengtu.kaichi.serviceimpl..*) && @annotation(com.mengtu.kaichi.serviceimpl.common.verify.VerifyPerm)")
    public void targetVerify() {
    }

    /**
     * 鉴权动作
     *
     * @param joinPoint
     * @param verify
     */
    @Before("targetVerify() && @annotation(verify)")
    public void verify(JoinPoint joinPoint, VerifyPerm verify) {
        Object[] objs = joinPoint.getArgs();
        VerifyRequest request = null;
        for (Object o : objs) {
            if (o instanceof VerifyRequest) {
                request = (VerifyRequest) o;
                break;
            }
        }
        AssertUtil.assertNotNull(request, CommonResultCode.SYSTEM_ERROR, "鉴权失败, 没有鉴权对象");

        boolean verifyPerm = userBasicService.verifyPermissionByPermType(request.getVerifyUserId(), Arrays.asList(verify.permType()));
        if (!verifyPerm) {
            LoggerUtil.warn(LOGGER, "用户无权操作 userId={0}, permType={1}", request.getVerifyUserId(), verify.permType());
            throw new KaiChiException(CommonResultCode.FORBIDDEN);
        }
    }
}
