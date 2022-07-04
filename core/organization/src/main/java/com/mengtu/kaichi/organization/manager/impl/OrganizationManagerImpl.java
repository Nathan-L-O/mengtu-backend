package com.mengtu.kaichi.organization.manager.impl;

import com.mengtu.kaichi.organization.dal.service.OrganizationMemberRepoService;
import com.mengtu.kaichi.organization.dal.service.OrganizationRelationRepoService;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.enums.MemberType;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.manager.OrganizationManager;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.organization.model.OrganizationRelationBO;
import com.mengtu.kaichi.organization.request.OrganizationManageRequest;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组织管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:47
 */
@Service
public class OrganizationManagerImpl implements OrganizationManager {

    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationManagerImpl.class);

    @Resource
    private OrganizationRepoService organizationRepoService;

    @Resource
    private OrganizationMemberRepoService organizationMemberRepoService;

    @Resource
    private OrganizationRelationRepoService organizationRelationRepoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationBO createOrganization(OrganizationManageRequest request) {
        OrganizationBO organizationBO = new OrganizationBO();
        organizationBO.setOrganizationName(request.getOrganizationName());
        organizationBO.setOrganizationType(request.getOrganizationType());
        //如果存在组织id则加入
        if (StringUtils.isNotBlank(request.getOrganizationId())) {
            organizationBO.setOrganizationId(request.getOrganizationId());
        }
        organizationBO = organizationRepoService.create(organizationBO);
        request.setOrganizationId(organizationBO.getOrganizationId());

        // 创建主组织关系
        if (StringUtils.isNotBlank(request.getPrimaryOrganizationId())) {
            OrganizationRelationBO relationBO = new OrganizationRelationBO();
            relationBO.setPrimaryOrganizationId(request.getPrimaryOrganizationId());
            relationBO.setSubOrganizationId(organizationBO.getOrganizationId());
            organizationRelationRepoService.createRelation(relationBO);
        }

        // 创建主管关系
        if (StringUtils.isNotBlank(request.getMemberId())) {
            // 防止未设置
            request.setMemberType(MemberType.PRINCIPAL);
            OrganizationMemberBO memberBO = parseMember(request);
            organizationMemberRepoService.addMember(memberBO);
        }
        return organizationBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disbandOrganization(OrganizationManageRequest request) {
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        if (organizationBO != null) {
            // 解除所有成员关系
            organizationMemberRepoService.disband(organizationBO.getOrganizationId());
            // 解除所有相关组织关系
            organizationRelationRepoService.disband(organizationBO.getOrganizationId());
            // 删除组织
            organizationRepoService.disband(organizationBO.getOrganizationId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void manageMember(OrganizationManageRequest request) {
        if (request.getMemberType() == MemberType.PRINCIPAL) {
            // 如果是主管变更 需要涉及特殊逻辑
            changePrincipal(request);
        } else {
            // 成员和管理员直接操作就行
            saveMember(parseMember(request));
        }
    }

    @Override
    public List<OrganizationBO> queryOrganizationByMemberId(String memberId) {
        List<String> organizationIds = CollectionUtil.toStream(organizationMemberRepoService.queryOrganizations(memberId))
                .filter(Objects::nonNull)
                .map(OrganizationMemberBO::getOrganizationId)
                .collect(Collectors.toList());
        return organizationRepoService.queryByOrganizationIds(organizationIds);
    }

    @Override
    public List<OrganizationMemberBO> queryOrganizationMemberByMemberId(String memberId) {
        return CollectionUtil.toStream(organizationMemberRepoService.queryOrganizations(memberId))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrganizationBO> queryAllOrganization() {
        return CollectionUtil.toStream(organizationRepoService.queryAllOrganization())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * 主管变更 需要把原有主管变为成员
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    void changePrincipal(OrganizationManageRequest request) {
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        if (organizationBO == null) {
            LoggerUtil.warn(LOGGER, "组织不存在 OrganizationId={0}", request.getOrganizationId());
            throw new KaiChiException(OrganizationErrorCode.ORGANIZATION_NOT_EXIST);
        }
        // 获取主管
        OrganizationMemberBO principal = organizationMemberRepoService.queryPrincipal(request.getOrganizationId());
        // 有主管时 需要将原来主管改成普通成员
        if (principal != null) {
            // 已经是主管就不需要重复设置
            if (StringUtils.equals(principal.getMemberId(), request.getMemberId())) {
                LoggerUtil.warn(LOGGER, "已是主管");
                return;
            }
            // 未指定新称呼 沿用老称呼
            if (StringUtils.isBlank(request.getMemberDesc())) {
                request.setMemberDesc(principal.getMemberDescription());
            }
            // 原来主管改成普通成员
            principal.setMemberType(MemberType.MEMBER.getType());
            principal.setMemberDescription(MemberType.MEMBER.getDesc());
            organizationMemberRepoService.updateMember(principal);
        }
        saveMember(parseMember(request));
    }

    /**
     * 保存成员关系  无则创  有则改
     *
     * @param memberBO
     */
    private void saveMember(OrganizationMemberBO memberBO) {
        OrganizationMemberBO preMemberBO = organizationMemberRepoService.queryMember(memberBO.getOrganizationId(), memberBO.getMemberId());
        if (preMemberBO == null) {
            organizationMemberRepoService.addMember(memberBO);
        } else {
            memberBO.setOrganizationMemberId(preMemberBO.getOrganizationMemberId());
            organizationMemberRepoService.updateMember(memberBO);
        }
    }


    /**
     * 解析成员信息
     *
     * @param request
     * @return
     */
    private OrganizationMemberBO parseMember(OrganizationManageRequest request) {
        OrganizationMemberBO memberBO = new OrganizationMemberBO();
        memberBO.setOrganizationId(request.getOrganizationId());
        memberBO.setOrganizationName(organizationRepoService.queryByOrganizationId(request.getOrganizationId()).getOrganizationName());
        memberBO.setMemberId(request.getMemberId());
        memberBO.setMemberType(request.getMemberType().getType());
        // 是否传入称呼描述
        if (StringUtils.isNotBlank(request.getMemberDesc())) {
            memberBO.setMemberDescription(request.getMemberDesc());
        } else {
            // 使用默认
            memberBO.setMemberDescription(request.getMemberType().getDesc());
        }
        return memberBO;
    }
}