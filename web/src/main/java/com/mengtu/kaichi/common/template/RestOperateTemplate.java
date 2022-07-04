package com.mengtu.kaichi.common.template;

import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;


/**
 * Rest 操作模板类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:57
 */
public class RestOperateTemplate {

    /**
     * 操作模板
     *
     * @param logger     日志
     * @param methodName 方法名称 标识
     * @param request    请求
     * @param callBack   操作方法
     * @param <T>        结果实体
     * @return
     */
    public static <T> Result<T> operate(Logger logger, String methodName, Object request, RestOperateCallBack<T> callBack) {
        long start = System.currentTimeMillis();
        Result<T> result = null;
        try {
            //操作前置
            callBack.before();

            //执行操作
            result = callBack.execute();

            //操作后置
            callBack.after();
            return result;
        } catch (KaiChiException e) {
            LoggerUtil.warn(e, logger, "RestOperateTemplate.operate fail, methodName={0}, request={1}, errorCode={2}, errorMsg={3}", methodName, request, e.getErrorCode(), e.getMessage());
            result = new Result<>(false, convertCode(e.getErrorCode()), e.getMessage());
            return result;
        } catch (Exception e) {
            LoggerUtil.error(e, logger, "RestOperateTemplate.operate error, methodName={0}, request={1}", methodName, request);
            result = new Result<>(false, RestResultCode.SYSTEM_ERROR.getCode(), RestResultCode.SYSTEM_ERROR.getMessage());
            return result;
        } catch (Throwable t) {
            LoggerUtil.error(t, logger, "RestOperateTemplate.operate throwable, methodName={0}, request={1}", methodName, request);
            result = new Result<>(false, RestResultCode.SYSTEM_ERROR.getCode(), RestResultCode.SYSTEM_ERROR.getMessage());
            return result;
        } finally {
            long end = System.currentTimeMillis();
            LoggerUtil.info(logger, "RestOperateTemplate.operate final, methodName={0}, consume={1}ms, request={2}, result={3}", methodName, end - start, request, result);
        }
    }


    /**
     * 转换错误码为 http 请求错误码
     *
     * @param errorCode
     * @return
     */
    private static String convertCode(String errorCode) {

        // 处理系统错误码转换
        for (RestResultCode resultCode : RestResultCode.values()) {
            if (StringUtils.equals(errorCode, resultCode.name())) {
                return resultCode.getCode();
            }
        }

        // 处理 rest 错误码转换
        for (RestResultCode resultCode : RestResultCode.values()) {
            if (StringUtils.equals(errorCode, resultCode.getCode())) {
                return resultCode.getCode();
            }
        }
        return RestResultCode.SYSTEM_ERROR.getCode();
    }

}
