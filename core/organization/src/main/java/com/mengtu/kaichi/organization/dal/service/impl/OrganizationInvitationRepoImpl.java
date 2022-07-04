package com.mengtu.kaichi.organization.dal.service.impl;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.organization.dal.convert.EntityConverter;
import com.mengtu.kaichi.organization.dal.model.OrganizationInvitationDO;
import com.mengtu.kaichi.organization.dal.repo.OrganizationInvitationRepo;
import com.mengtu.kaichi.organization.dal.service.OrganizationInvitationRepoService;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.idfactory.BizIdFactory;
import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 组织邀请仓储服务实现
 * <p>
 * \ * @author 过昊天
 *
 * @version 1.1 @ 2022/6/6 10:39
 */
@Service
public class OrganizationInvitationRepoImpl implements OrganizationInvitationRepoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationRepoServiceImpl.class);

    @Resource
    OrganizationInvitationRepo organizationInvitationRepo;

    @Resource
    BizIdFactory organizationBizIdFactory;

    @Override
    public void createInvitation(OrganizationInvitationBO organizationInvitationBO) {
        AssertUtil.assertNotNull(organizationInvitationBO);

        String organizationId = organizationInvitationBO.getOrganizationId();
        String memberId = organizationInvitationBO.getMemberId();
        AssertUtil.assertStringNotBlank(organizationId, OrganizationErrorCode.INVALID_ORGANIZATION_ID);
        AssertUtil.assertStringNotBlank(memberId, "成员 ID 不能为空");

        OrganizationInvitationDO organizationInvitationDO =
                organizationInvitationRepo.findByOrganizationIdAndMemberId(organizationId, memberId);
        if (organizationInvitationDO != null) {
            LoggerUtil.warn(LOGGER, "重复创建邀请 organizationInvitationDO={0}", organizationInvitationDO);
            return;
        }

        // 未有申请记录 则创建关系
        if (StringUtils.isBlank(organizationInvitationBO.getOrganizationInvitationId())) {
            organizationInvitationBO.setOrganizationInvitationId(organizationBizIdFactory.getOrganizationInvitation(organizationId, memberId));
        }

        organizationInvitationBO.setEncodeUrl(HashUtil.sha256(
                organizationInvitationBO.getOrganizationInvitationId() +
                        organizationInvitationBO.getLifetime().toString()
        ));
        organizationInvitationRepo.save(EntityConverter.convert(organizationInvitationBO));
    }

    @Override
    public void updateInvitation(OrganizationInvitationBO organizationInvitationBO) {
        AssertUtil.assertNotNull(organizationInvitationBO);

        OrganizationInvitationDO organizationInvitationDO = null;
        if (StringUtils.isNotBlank(organizationInvitationBO.getOrganizationInvitationId())) {
            organizationInvitationDO = organizationInvitationRepo.findByOrganizationInvitationId(organizationInvitationBO.getOrganizationInvitationId());
            AssertUtil.assertNotNull(organizationInvitationDO, "邀请关系不存在");
        } else {
            String organizationId = organizationInvitationBO.getOrganizationId();
            String memberId = organizationInvitationBO.getMemberId();
            AssertUtil.assertStringNotBlank(organizationId, OrganizationErrorCode.INVALID_ORGANIZATION_ID);
            AssertUtil.assertStringNotBlank(memberId, "成员 ID 不能为空");
            organizationInvitationDO = organizationInvitationRepo.findByOrganizationIdAndMemberId(organizationId, memberId);
            AssertUtil.assertNotNull(organizationInvitationDO, "邀请关系不存在");
        }

        organizationInvitationDO.setLifetime(organizationInvitationBO.getLifetime());

        organizationInvitationDO.setEncodeUrl(HashUtil.sha256(
                organizationInvitationBO.getOrganizationInvitationId() +
                        organizationInvitationBO.getLifetime().toString()
        ));

        if (organizationInvitationBO.getExtInfo() == null) {
            organizationInvitationDO.setExtInfo(JSON.toJSONString(new HashMap<>()));
        } else if (!organizationInvitationBO.getExtInfo().isEmpty()) {
            organizationInvitationDO.setExtInfo(JSON.toJSONString(organizationInvitationBO.getExtInfo()));
        }
        organizationInvitationRepo.save(organizationInvitationDO);
    }

    @Override
    public OrganizationInvitationBO queryInvitationByUrl(String encodeUrl) {
        OrganizationInvitationDO organizationInvitationDO = organizationInvitationRepo.findByEncodeUrl(encodeUrl);
        AssertUtil.assertNotNull(organizationInvitationDO, RestResultCode.NOT_FOUND, "邀请链接不存在");
        return EntityConverter.convert(organizationInvitationDO);
    }

    @Override
    public OrganizationInvitationBO queryInvitationByOrganizationIdAndMemberId(String organizationId, String memberId) {
        return EntityConverter.convert(organizationInvitationRepo.findByOrganizationIdAndMemberId(organizationId, memberId));
    }

    @Override
    public OrganizationInvitationBO queryInvitationByOrganizationInvitationId(String organizationInvitationId) {
        OrganizationInvitationDO organizationInvitationDO = organizationInvitationRepo.findByOrganizationInvitationId(organizationInvitationId);
        AssertUtil.assertNotNull(organizationInvitationDO, RestResultCode.NOT_FOUND, "邀请 ID 不存在");
        return EntityConverter.convert(organizationInvitationDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String organizationInvitationId) {
        organizationInvitationRepo.deleteByOrganizationInvitationId(organizationInvitationId);
    }
}