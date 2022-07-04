package com.mengtu.kaichi.common.log;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.common.BaseRestRequest;
import com.mengtu.kaichi.common.RestAop;
import com.mengtu.kaichi.util.IPUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.log.Log;
import com.mengtu.util.log.LogLevel;
import com.mengtu.util.log.LogMark;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * 用户日志摘要，@Transactional 前执行
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:56
 */
@Order(-1)
@Aspect
@Component
public class LogDigest extends RestAop {

    /**
     * 结果摘要日志模板
     */
    private final static String DIGEST_TEMPLATE = "[{0}] ip=[{1}], action=[{2}], result=[{3}], time=[{4}], errorMessage=[{5}], params={6}";

    @Pointcut("execution(* com.mengtu.kaichi.controller..*(..)) && @annotation(com.mengtu.util.log.Log)")
    public void targetLog() {
    }

    @Around("targetLog() && @annotation(log)")
    public Object loggerParse(ProceedingJoinPoint proceedingJoinPoint, Log log) throws Throwable {
        final Logger logger = LoggerFactory.getLogger(log.loggerName());

        BaseRestRequest request = parseRestRequest(proceedingJoinPoint);
        HttpServletRequest httpServletRequest = parseHttpServletRequest(proceedingJoinPoint);

        AssertUtil.assertNotNull(request, CommonResultCode.SYSTEM_ERROR, "请求解析失败");
        AssertUtil.assertNotNull(httpServletRequest, CommonResultCode.SYSTEM_ERROR, "请求解析失败");

        String ip = parseIp(httpServletRequest);
        String methodName = proceedingJoinPoint.getSignature().getName();

        String resultMessage;
        long start = System.currentTimeMillis();
        Result result = (Result) proceedingJoinPoint.proceed();
        long end = System.currentTimeMillis();
        resultMessage = MessageFormat.format(DIGEST_TEMPLATE, log.identity(), ip, methodName, parseResult(result), (end - start) + "ms", result.getErrorMsg(), parseParams(request));
        log(logger, log.logLevel(), resultMessage);
        return result;
    }

    /**
     * 解析结果
     *
     * @param result
     * @return
     */
    private String parseResult(Result result) {
        return result.isSuccess() ? LogMark.SUCCESS : LogMark.FAIL;
    }

    /**
     * 解析 IP
     *
     * @param request
     * @return
     */
    private String parseIp(HttpServletRequest request) {
        String ip = IPUtil.getIp(request);
        return StringUtils.isBlank(ip) ? LogMark.DEFAULT : ip;
    }

    /**
     * 解析参数
     *
     * @param restRequest
     * @return
     */
    private String parseParams(BaseRestRequest restRequest) {
        Map<String, String> params = new HashMap<>(1);
        String userId = restRequest.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            params.put("userId", userId);
        }
        return JSON.toJSONString(params);
    }

    /**
     * 打印日志
     *
     * @param logger
     * @param logLevel
     * @param message
     */
    private void log(Logger logger, LogLevel logLevel, String message) {
        AssertUtil.assertNotNull(logLevel, CommonResultCode.ILLEGAL_PARAMETERS, "日志级别不能为空");
        AssertUtil.assertNotNull(logger, CommonResultCode.ILLEGAL_PARAMETERS, "logger 不能为空");

        switch (logLevel) {
            case INFO:
                logger.info(message);
                break;
            case WARN:
                logger.warn(message);
                break;
            case ERROR:
                logger.error(message);
                break;
            case DEBUG:
                logger.debug(message);
                break;
            case TRACE:
                logger.trace(message);
                break;
            default:
                throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS, "日志级别无效");
        }
    }

}
