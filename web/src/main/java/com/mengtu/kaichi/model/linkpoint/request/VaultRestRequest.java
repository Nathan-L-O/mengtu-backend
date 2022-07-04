package com.mengtu.kaichi.model.linkpoint.request;

import com.mengtu.kaichi.common.BaseRestRequest;

/**
 * Vault 请求
 *
 * @author 过昊天
 * @version 1.2 @ 2022/6/1 14:03
 */
public class VaultRestRequest extends BaseRestRequest {

    private static final long serialVersionUID = 2472960790013258355L;
    private String modelId;

    private String uri;

    private Integer sort;

    private String hashtag;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String toAuditString() {
        return uri + hashtag;
    }
}
