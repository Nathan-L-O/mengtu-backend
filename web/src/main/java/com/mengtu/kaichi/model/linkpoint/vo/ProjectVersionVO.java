package com.mengtu.kaichi.model.linkpoint.vo;

import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;

/**
 * 项目版本视图对象
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:52
 */
public class ProjectVersionVO extends ProjectVersionBO {

    private static final long serialVersionUID = 815093499311288025L;
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