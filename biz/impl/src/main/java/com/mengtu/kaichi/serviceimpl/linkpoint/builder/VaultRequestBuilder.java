package com.mengtu.kaichi.serviceimpl.linkpoint.builder;

import com.mengtu.kaichi.serviceimpl.linkpoint.request.VaultRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * linkpoint Vault 请求构建器
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:39
 */
final public class VaultRequestBuilder {

    /**
     * 请求 ID
     */
    private String requestId;

    /**
     * 操作员 ID
     */
    private String userId;

    /**
     * 模型 ID
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 模型状态
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 标签
     */
    private String hashtag;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public static VaultRequestBuilder getInstance() {
        return new VaultRequestBuilder();
    }

    public VaultRequest build() {
        VaultRequest request = new VaultRequest();
        request.setRequestId(requestId);
        request.setSort(sort);
        request.setUserId(userId);
        request.setStatus(status);
        request.setModelId(modelId);
        request.setModelName(modelName);
        request.setExtInfo(extInfo);
        request.setHashtag(hashtag);
        return request;
    }

    private VaultRequestBuilder() {
    }

    public VaultRequestBuilder withRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public VaultRequestBuilder withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public VaultRequestBuilder withSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public VaultRequestBuilder withStatus(Integer status) {
        this.status = status;
        return this;
    }

    public VaultRequestBuilder withModelId(String modelId) {
        this.modelId = modelId;
        return this;
    }

    public VaultRequestBuilder withModelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public VaultRequestBuilder withHashtag(String hashtag) {
        this.hashtag = hashtag;
        return this;
    }

    public VaultRequestBuilder withExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
        return this;
    }

}