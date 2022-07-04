package com.mengtu.kaichi.model.linkpoint.vo;

import com.mengtu.kaichi.linkpoint.model.ProjectBO;

/**
 * 项目视图对象
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:52
 */
public class ProjectVO extends ProjectBO {

    private static final long serialVersionUID = -1736345395914167599L;

    /**
     * 签名 URL
     */
    private String signatureUrl;

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public void setSignatureUrl(String signatureUrl) {
        this.signatureUrl = signatureUrl;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}