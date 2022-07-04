package com.mengtu.kaichi.serviceimpl.linkpoint.request;

import com.mengtu.kaichi.linkpoint.request.VaultManageRequest;
import com.mengtu.kaichi.serviceimpl.common.verify.VerifyRequest;

/**
 * linkpoint Vault 请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:39
 */
public class VaultRequest extends VaultManageRequest implements VerifyRequest {

    private static final long serialVersionUID = -6987268223528658033L;

    /**
     * 请求 ID
     */
    private String requestId;

    /**
     * 操作员 ID
     */
    private String userId;

    /**
     * 标签
     */
    private String hashtag;

    /**
     * fdf
     */
    private Integer sort;

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public String getHashtag() {
        return hashtag;
    }

    @Override
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String getVerifyUserId() {
        return userId;
    }
}