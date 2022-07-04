package com.mengtu.kaichi.linkpoint.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * linkpoint vault 实体
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:40
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "linkpoint_vault",
        indexes = {
                @Index(name = "uk_model_id", columnList = "model_id", unique = true)
        })
public class LinkpointVaultDO extends BaseDO {

    private static final long serialVersionUID = -7057281635350789794L;

    /**
     * 项目 ID
     */
    @Column(name = "model_id", length = 32, nullable = false)
    private String modelId;

    /**
     * 项目名称
     */
    @Column(name = "model_name", nullable = false)
    private String modelName;

    /**
     * 项目状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 标签
     */
    @Column(name = "hashtag")
    private String hashtag;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}