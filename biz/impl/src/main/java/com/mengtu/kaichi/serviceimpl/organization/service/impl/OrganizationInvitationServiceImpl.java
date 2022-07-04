package com.mengtu.kaichi.serviceimpl.organization.service.impl;

import com.mengtu.kaichi.organization.dal.service.OrganizationInvitationRepoService;
import com.mengtu.kaichi.organization.enums.MemberType;
import com.mengtu.kaichi.organization.manager.OrganizationManager;
import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.organization.builder.OrganizationRequestBuilder;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationInvitationService;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationManagerService;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.tools.AssertUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 组织邀请服务实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/6 14:09
 */
@Service
public class OrganizationInvitationServiceImpl implements OrganizationInvitationService {

    @Resource
    OrganizationInvitationRepoService organizationInvitationRepoService;

    @Resource
    UserBasicService userBasicService;

    @Resource
    OrganizationManager organizationManager;

    @Resource
    OrganizationService organizationService;

    @Resource
    OrganizationManagerService organizationManagerService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationInvitationBO createInvitation(OrganizationRequest request) {
        OrganizationInvitationBO organizationInvitationBO =
                organizationInvitationRepoService.queryInvitationByOrganizationIdAndMemberId(request.getOrganizationId(), request.getUserId());
        if (organizationInvitationBO == null) {
            organizationInvitationBO = new OrganizationInvitationBO();
            organizationInvitationBO.setOrganizationId(request.getOrganizationId());
            organizationInvitationBO.setMemberId(request.getUserId());
            organizationInvitationBO.setLifetime(request.getLifetime());
            organizationInvitationRepoService.createInvitation(organizationInvitationBO);
            organizationInvitationBO =
                    organizationInvitationRepoService.queryInvitationByOrganizationIdAndMemberId(request.getOrganizationId(), request.getUserId());
        } else if (request.getLifetime().compareTo(organizationInvitationBO.getLifetime()) != 0) {
            AssertUtil.assertTrue(request.getLifetime().after(organizationInvitationBO.getLifetime()), RestResultCode.ILLEGAL_PARAMETERS, "更新日期不能早于现有日期");
            organizationInvitationBO.setLifetime(request.getLifetime());
            organizationInvitationRepoService.updateInvitation(organizationInvitationBO);
            organizationInvitationBO =
                    organizationInvitationRepoService.queryInvitationByOrganizationIdAndMemberId(request.getOrganizationId(), request.getUserId());
        }

        return organizationInvitationBO;
    }

    @Override
    public OrganizationInvitationBO queryInvitation(OrganizationRequest request) {
        return organizationInvitationRepoService.queryInvitationByOrganizationIdAndMemberId(request.getOrganizationId(), request.getUserId());
    }

    @Override
    public OrganizationInvitationBO getInvitation(OrganizationRequest request) {
        OrganizationInvitationBO organizationInvitationBO = organizationInvitationRepoService.queryInvitationByUrl(request.getEncodeUrl());
        AssertUtil.assertTrue(organizationInvitationBO.getLifetime().after(new Date()), "邀请已过期, 请联系发起人重置");

        Map<String, String> map = new HashMap<>(2);
        UserInfoBO userInfoBO = userBasicService.getByUserId(organizationInvitationBO.getMemberId()).getUserInfo();

        map.put("inviterMobilePhone", userInfoBO.getMobilePhone());
        map.put("inviterNickname", userInfoBO.getNickname());
        List<OrganizationMemberBO> list = organizationManager.queryOrganizationMemberByMemberId(organizationInvitationBO.getMemberId());
        for (OrganizationMemberBO organizationMemberBO : list) {
            if (Objects.equals(organizationMemberBO.getOrganizationId(), organizationInvitationBO.getOrganizationId())) {
                map.put("inviterOrganization", organizationMemberBO.getOrganizationName());
            }
        }

        organizationInvitationBO.setExtInfo(map);

        return organizationInvitationBO;
    }

    @Override
    public void acceptInvitation(OrganizationRequest request) {
        OrganizationInvitationBO organizationInvitationBO =
                organizationInvitationRepoService.queryInvitationByOrganizationInvitationId(request.getOrganizationInvitationId());
        AssertUtil.assertTrue(organizationInvitationBO.getLifetime().after(new Date()));

        OrganizationRequestBuilder builder = OrganizationRequestBuilder.getInstance()
                .withOrganizationId(organizationInvitationBO.getOrganizationId());

        List<List<OrganizationMemberBO>> list = organizationService.queryOrganizationMemberByOrganizationId(builder.build());
        String permUserId = "";
        for (List<OrganizationMemberBO> organizationMemberBOS : list) {
            if (organizationMemberBOS.size() != 0) {
                for (OrganizationMemberBO organizationMemberBO : organizationMemberBOS) {
                    AssertUtil.assertNotEquals(organizationMemberBO.getMemberId(), request.getUserId(), "已在团队中");
                    if (Objects.equals(organizationMemberBO.getMemberType(), MemberType.PRINCIPAL.getType())) {
                        permUserId = organizationMemberBO.getMemberId();
                    }
                }
            }
        }
        builder = OrganizationRequestBuilder.getInstance()
                .withUserId(permUserId)
                .withOrganizationId(organizationInvitationBO.getOrganizationId())
                .withAttachUserId(request.getUserId())
                .withMemberType("MEMBER");
        organizationManagerService.addMember(builder.build());
    }
}