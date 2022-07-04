package com.mengtu.kaichi.organization.dal.service.impl;

import com.mengtu.kaichi.organization.dal.convert.EntityConverter;
import com.mengtu.kaichi.organization.dal.model.OrganizationRelationDO;
import com.mengtu.kaichi.organization.dal.repo.OrganizationRelationRepo;
import com.mengtu.kaichi.organization.dal.service.OrganizationRelationRepoService;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.idfactory.BizIdFactory;
import com.mengtu.kaichi.organization.model.OrganizationRelationBO;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 组织关系仓储服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:41
 */
@Service
public class OrganizationRelationRepoImpl implements OrganizationRelationRepoService {

    @Autowired
    private OrganizationRelationRepo organizationRelationRepo;

    @Autowired
    private BizIdFactory organizationBizIdFactory;

    @Override
    public OrganizationRelationBO createRelation(OrganizationRelationBO relationBO) {
        AssertUtil.assertTrue(!StringUtils.equals(relationBO.getPrimaryOrganizationId(), relationBO.getSubOrganizationId()), "同组织不能生成关系");
        // 检查是否存在组织关系
        OrganizationRelationDO relationDO = organizationRelationRepo.findByPrimaryOrganizationIdAndSubOrganizationId(relationBO.getPrimaryOrganizationId(), relationBO.getSubOrganizationId());
        if (relationDO != null) {
            return EntityConverter.convert(relationDO);
        }
        // 检查是否存在相反组织关系
        relationDO = organizationRelationRepo.findByPrimaryOrganizationIdAndSubOrganizationId(relationBO.getSubOrganizationId(), relationBO.getPrimaryOrganizationId());
        AssertUtil.assertNull(relationDO, "已存在相反的组织关系");

        // 创建组织关系
        if (StringUtils.isBlank(relationBO.getOrganizationRelationId())) {
            relationBO.setOrganizationRelationId(organizationBizIdFactory.getOrganizationId());
        }
        return EntityConverter.convert(organizationRelationRepo.save(EntityConverter.convert(relationBO)));
    }

    @Override
    public void removeRelation(String primaryOrganizationId, String subOrganizationId) {
        // 检查是否存在组织关系
        OrganizationRelationDO relationDO = organizationRelationRepo.findByPrimaryOrganizationIdAndSubOrganizationId(primaryOrganizationId, subOrganizationId);
        if (relationDO != null) {
            // 存在则删除
            organizationRelationRepo.delete(relationDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disband(String organizationId) {
        AssertUtil.assertStringNotBlank(organizationId, OrganizationErrorCode.INVALID_ORGANIZATION_ID);
        organizationRelationRepo.deleteByPrimaryOrganizationId(organizationId);
        organizationRelationRepo.deleteBySubOrganizationId(organizationId);
    }
}