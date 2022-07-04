package com.mengtu.kaichi.common;


import com.mengtu.util.common.ToString;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * rest 请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:57
 */
public abstract class BaseRestRequest extends ToString {

    private static final long serialVersionUID = -4306764132322568740L;

    /**
     * 请求id
     */
    @NotBlank
    private String requestId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    /**
     * 放入拓展信息
     *
     * @param key
     * @param value
     */
    public void putExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>(10);
        }
        extInfo.put(key, value);
    }

    /**
     * 取出拓展信息
     *
     * @param key
     * @return
     */
    public String fetchExtInfo(String key) {
        if (extInfo == null) {
            return null;
        }
        return extInfo.get(key);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
