package com.mengtu.kaichi.util;

import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;

/**
 * restful 结果处理工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:59
 */
public class RestResultUtil {

    /**
     * 构建失败结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildFailResult(T data, String message) {
        Result<T> result = buildFailResult(message);
        result.setData(data);
        return result;
    }

    /**
     * 构建失败结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildFailResult(T data) {
        Result<T> result = buildFailResult();
        result.setData(data);
        return result;
    }

    /**
     * 构建失败结果
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildFailResult() {
        Result<T> result = buildResult(RestResultCode.SYSTEM_ERROR);
        result.setSuccess(false);
        result.setRetry(false);
        return result;
    }

    /**
     * 构建失败结果
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildFailResult(String message) {
        Result<T> result = buildResult(RestResultCode.SYSTEM_ERROR, message);
        result.setSuccess(false);
        result.setRetry(false);
        return result;
    }

    /**
     * 构建成功结果
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildSuccessResult(T data) {
        Result<T> result = buildSuccessResult();
        result.setData(data);
        return result;
    }

    /**
     * 构建成功结果
     *
     * @param data
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildSuccessResult(T data, String message) {
        Result<T> result = buildSuccessResult(message);
        result.setData(data);
        return result;
    }

    /**
     * 构建成功结果
     *
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildSuccessResult() {
        Result<T> result = buildResult(RestResultCode.SUCCESS);
        result.setSuccess(true);
        result.setRetry(false);
        return result;
    }

    /**
     * 构建成功结果
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildSuccessResult(String message) {
        Result<T> result = buildResult(RestResultCode.SUCCESS, message);
        result.setSuccess(true);
        result.setRetry(false);
        return result;
    }

    /**
     * 构建结果
     *
     * @param resultCode
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildResult(RestResultCode resultCode) {
        Result<T> result = new Result<>();
        result.setErrorCode(resultCode.getCode());
        result.setErrorMsg(resultCode.getMessage());
        return result;
    }

    /**
     * 构建结果
     *
     * @param resultCode
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Result<T> buildResult(RestResultCode resultCode, String message) {
        Result<T> result = new Result<>();
        result.setErrorCode(resultCode.getCode());
        result.setErrorMsg(message);
        result.setRetry(false);
        result.setSuccess(resultCode == RestResultCode.SUCCESS);
        return result;
    }

}
