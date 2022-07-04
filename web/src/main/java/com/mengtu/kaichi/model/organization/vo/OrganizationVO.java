package com.mengtu.kaichi.model.organization.vo;

import com.mengtu.kaichi.organization.model.OrganizationBO;

/**
 * 组织展示对象
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:52
 */
public class OrganizationVO extends OrganizationBO {

    private static final long serialVersionUID = 9001055436953599817L;

    /**
     * 首字母
     */
    private String firstAlpha;

    public String getFirstAlpha() {
        return firstAlpha;
    }

    public void setFirstAlpha(String firstAlpha) {
        this.firstAlpha = firstAlpha;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}