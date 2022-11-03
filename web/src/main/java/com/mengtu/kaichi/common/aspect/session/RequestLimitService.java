package com.mengtu.kaichi.common.aspect.session;

import com.mengtu.kaichi.common.BaseRestRequest;
import com.mengtu.kaichi.common.RestAop;
import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.tools.AssertUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rest 切面请求限制(登录态)
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/2 14:20
 */
@Order(2)
@Aspect
@Component
public class RequestLimitService extends RestAop {

    /**
     * 计划任务 Quartz Cron 表达式 : CleanCycle
     */
    private static final String CORN_STRING_CLEAN = "0 */1 * * * ?";

    private static final int MILLI_TO_SECOND = 1000;

    /**
     * 校验容器
     */
    private static final Map<String, Long> POOL = new ConcurrentHashMap<>();

    @Pointcut("execution(* com.mengtu.kaichi.controller..*(..)) && @annotation(com.mengtu.kaichi.common.annotation.Limit)")
    public void limitRequest() {
    }

    /**
     * 校验
     *
     * @param proceedingJoinPoint
     * @param limitInstance
     * @return
     * @throws Throwable
     */
    @Around(value = "limitRequest() && @annotation(limitInstance)")
    public Object limitRequest(ProceedingJoinPoint proceedingJoinPoint, Limit limitInstance) throws Throwable {
        BaseRestRequest request = parseRestRequest(proceedingJoinPoint);
        HttpServletRequest httpServletRequest = parseHttpServletRequest(proceedingJoinPoint);
        AssertUtil.assertNotNull(httpServletRequest, CommonResultCode.SYSTEM_ERROR, "请求解析失败");
        AssertUtil.assertNotNull(request, CommonResultCode.SYSTEM_ERROR, "请求解析失败");
        long current = System.currentTimeMillis() / MILLI_TO_SECOND;

        String browserSessionKey = HashUtil.md5(httpServletRequest.getSession().getId() + httpServletRequest.getServletPath());
        String userSessionKey = HashUtil.md5(request.getUserId() + httpServletRequest.getServletPath());

//        if (POOL.containsKey(browserSessionKey)) {
//            if (POOL.get(browserSessionKey) + limitInstance.threshold() > current) {
//                return RestResultUtil.buildResult(RestResultCode.FORBIDDEN, String.format("请求过于频繁，请 %d 秒后再试",
//                        POOL.get(browserSessionKey) + (long) limitInstance.threshold() - current));
//            }
//        }
//        if (POOL.containsKey(userSessionKey)) {
//            if (POOL.get(userSessionKey) + limitInstance.threshold() > current) {
//                return RestResultUtil.buildResult(RestResultCode.FORBIDDEN, String.format("请求过于频繁，请 %d 秒后再试",
//                        POOL.get(userSessionKey) + (long) limitInstance.threshold() - current));
//            }
//        }
        POOL.put(browserSessionKey, System.currentTimeMillis() / MILLI_TO_SECOND);
        POOL.put(userSessionKey, System.currentTimeMillis() / MILLI_TO_SECOND);
        return proceedingJoinPoint.proceed();
    }

    /**
     * 自动化管理。
     */
    @Scheduled(cron = CORN_STRING_CLEAN)
    private void cleanCycle() {
        long currTime = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, Long> i : POOL.entrySet()) {
            if (i.getValue() + 3600 < currTime) {
                POOL.remove(i.getKey());
            }
        }
    }


}
