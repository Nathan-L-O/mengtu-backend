package com.mengtu.kaichi.organization.dal.service.impl;

import com.mengtu.kaichi.organization.dal.convert.EntityConverter;
import com.mengtu.kaichi.organization.dal.model.OrganizationDO;
import com.mengtu.kaichi.organization.dal.repo.OrganizationRepo;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.idfactory.BizIdFactory;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组织仓储服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:41
 */
@Service
public class OrganizationRepoServiceImpl implements OrganizationRepoService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationRepoServiceImpl.class);

    private static final String ORGANIZATION_AVATAR_LOCATION = "organization/avatar/";

    @Resource
    private OrganizationRepo organizationRepo;

    @Resource
    private BizIdFactory organizationBizIdFactory;

    @Resource
    private ObsUtil obsUtil;


    @Override
    public OrganizationBO create(OrganizationBO organizationBO) {
        AssertUtil.assertStringNotBlank(organizationBO.getOrganizationName(), OrganizationErrorCode.INVALID_ORGANIZATION_NAME);
        OrganizationDO organizationDO = organizationRepo.findByOrganizationName(organizationBO.getOrganizationName());
        AssertUtil.assertNull(organizationDO, "组织已存在");

        if (StringUtils.isBlank(organizationBO.getOrganizationId())) {
            organizationBO.setOrganizationId(organizationBizIdFactory.getOrganizationId());
        }
        return EntityConverter.convert(organizationRepo.save(EntityConverter.convert(organizationBO)));
    }

    @Override
    public void disband(String organizationId) {
        AssertUtil.assertStringNotBlank(organizationId, OrganizationErrorCode.INVALID_ORGANIZATION_ID);
        organizationRepo.deleteByOrganizationId(organizationId);
    }

    @Override
    public OrganizationBO modify(OrganizationBO organizationBO) {
        OrganizationDO organizationDO = organizationRepo.findByOrganizationId(organizationBO.getOrganizationId());
        if (organizationDO == null) {
            LoggerUtil.error(LOGGER, "更新的组织不存在 organizationId={0}", organizationBO.getOrganizationId());
            throw new KaiChiException(CommonResultCode.ILLEGAL_PARAMETERS.getCode(), OrganizationErrorCode.ORGANIZATION_NOT_EXIST.getMessage());
        }

        OrganizationDO newOrganizationDO = EntityConverter.convert(organizationBO);

        if (newOrganizationDO.getOrganizationName() != null) {
            organizationDO.setOrganizationName(newOrganizationDO.getOrganizationName());
        }
        if (newOrganizationDO.getOrganizationType() != null) {
            organizationDO.setOrganizationType(newOrganizationDO.getOrganizationType());
        }
        if (!organizationBO.getExtInfo().isEmpty()) {
            organizationDO.setExtInfo(newOrganizationDO.getExtInfo());
        }
        return EntityConverter.convert(organizationRepo.save(organizationDO));
    }

    @Override
    public List<OrganizationBO> queryAllOrganization() {
        return CollectionUtil.toStream(organizationRepo.findAll())
                .filter(Objects::nonNull)
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationBO queryByOrganizationId(String organizationId) {
        OrganizationBO organizationBO = EntityConverter.convert(organizationRepo.findByOrganizationId(organizationId));
        try {
            organizationBO.putExtInfo("avatarUrl", obsUtil.getSignatureDownloadUrl(
                    ORGANIZATION_AVATAR_LOCATION,
                    HashUtil.sha256(organizationId),
                    120L));
        } catch (Exception ignored) {
        }
        return organizationBO;
    }

    @Override
    public List<OrganizationBO> queryByOrganizationIds(List<String> organizationId) {
        return CollectionUtil.toStream(organizationRepo.findAllByOrganizationIdIn(organizationId))
                .filter(Objects::nonNull)
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationBO queryByOrganizationName(String organizationName) {
        return EntityConverter.convert(organizationRepo.findByOrganizationName(organizationName));
    }
}