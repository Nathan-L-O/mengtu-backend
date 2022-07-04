package com.mengtu.kaichi.converter.organizaion;

import com.mengtu.kaichi.model.organization.vo.OrganizationVO;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.util.dictionary.PinyinUtils;

/**
 * 组织VO转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:32
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class OrganizationVOConverter {

    /**
     * 转换组织VO
     *
     * @param organizationBO
     * @return
     */
    public static OrganizationVO convert(OrganizationBO organizationBO) {
        OrganizationVO organizationVO = new OrganizationVO();
        organizationVO.setOrganizationId(organizationBO.getOrganizationId());
        organizationVO.setOrganizationName(organizationBO.getOrganizationName());
        organizationVO.setOrganizationType(organizationBO.getOrganizationType());
        organizationVO.setExtInfo(organizationBO.getExtInfo());
        organizationVO.setFirstAlpha(PinyinUtils.getFirstAlpha(organizationVO.getOrganizationName()));
        return organizationVO;
    }

}