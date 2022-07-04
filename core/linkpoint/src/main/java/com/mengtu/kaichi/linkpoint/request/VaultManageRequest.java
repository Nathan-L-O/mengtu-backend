package com.mengtu.kaichi.linkpoint.request;

/**
 * Vault 管理请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 14:02
 */
public class VaultManageRequest extends BaseRequest {

    private static final long serialVersionUID = -6929699629274329791L;

    /**
     * 模型 ID
     */
    private String modelId;

    /**
     * 模型名称
     */
    private String modelName;

    /**
     * 标签
     */
    private String hashtag;

    /**
     * 模型状态
     */
    private Integer status;

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
