package com.mengtu.kaichi.converter.organizaion;

import com.mengtu.kaichi.model.organization.vo.OrganizationInvitationVO;
import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.util.dictionary.PinyinUtils;

/**
 * 组织邀请VO转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:32
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class OrganizationInvitationVOConverter {

    /**
     * 转换组织VO
     *
     * @param organizationInvitationBO
     * @return
     */
    public static OrganizationInvitationVO convert(OrganizationInvitationBO organizationInvitationBO) {
        OrganizationInvitationVO organizationInvitationVO = new OrganizationInvitationVO();
        organizationInvitationVO.setOrganizationId(organizationInvitationBO.getOrganizationId());
        organizationInvitationVO.setOrganizationInvitationId(organizationInvitationBO.getOrganizationInvitationId());
        organizationInvitationVO.setLifetime(organizationInvitationBO.getLifetime());
        organizationInvitationVO.setMemberId(organizationInvitationBO.getMemberId());
        organizationInvitationVO.setEncodeUrl(organizationInvitationBO.getEncodeUrl());
        organizationInvitationVO.setExtInfo(organizationInvitationBO.getExtInfo());
        organizationInvitationVO.setFirstAlpha(PinyinUtils.getFirstAlpha(organizationInvitationBO.getOrganizationInvitationId()));
        return organizationInvitationVO;
    }

}