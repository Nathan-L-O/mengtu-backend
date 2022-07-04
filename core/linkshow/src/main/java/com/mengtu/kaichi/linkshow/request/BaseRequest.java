package com.mengtu.kaichi.linkshow.request;

import com.mengtu.util.common.ToString;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 09:04
 */
public abstract class BaseRequest extends ToString {

    private static final long serialVersionUID = 8490001407584548282L;

    /**
     * 请求id
     */
    @NotNull
    private String requestId;

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
            extInfo = new HashMap<>();
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
}
