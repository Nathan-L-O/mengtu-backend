package com.mengtu.kaichi.model.organization.vo;

import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;

/**
 * 组织邀请展示对象
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/6 14:01
 */
public class OrganizationInvitationVO extends OrganizationInvitationBO {

    private static final long serialVersionUID = -2264577957850897129L;

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