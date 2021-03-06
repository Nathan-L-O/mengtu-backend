package com.mengtu.kaichi.serviceimpl.organization.service.impl;

import com.mengtu.kaichi.organization.dal.service.OrganizationMemberRepoService;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.enums.MemberType;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.manager.OrganizationManager;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.organization.request.OrganizationManageRequest;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationExtInfoKey;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermExInfoKey;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermType;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.user.dal.service.PermRepoService;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.enums.UserErrorCode;
import com.mengtu.kaichi.user.manager.PermManager;
import com.mengtu.kaichi.user.manager.UserManager;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;
import com.mengtu.kaichi.user.request.PermManageRequest;
import com.mengtu.kaichi.user.request.UserManageRequest;
import com.mengtu.kaichi.user.user.builder.PermBOBuilder;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.validator.MultiValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * ????????????????????????
 *
 * @author ?????????
 * @version 1.1 @ 2022/6/6 16:33
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Resource
    private OrganizationManager organizationManager;

    @Resource
    private OrganizationRepoService organizationRepoService;

    @Resource
    private OrganizationMemberRepoService organizationMemberRepoService;

    @Resource
    private PermManager permManager;

    @Resource
    private PermRepoService permRepoService;

    @Resource
    private UserManager userManager;

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private UserBasicService userBasicService;

    @Resource
    private MultiValidator<OrganizationManageRequest> organizationRegisterValidator;

    @Resource
    private ObsUtil obsUtil;

    /**
     * ?????? OBS ??????
     */
    private static final String AVATAR_LOCATION = "user/avatar/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationBO create(OrganizationRequest request) {
        organizationRegisterValidator.validate(request);
        if (StringUtils.isBlank(request.getMemberId()) && StringUtils.isNotBlank(request.getAttachUserId())) {
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getAttachUserId());
            AssertUtil.assertNotNull(userInfoBO, "?????????????????????????????????");
            request.setMemberId(userInfoBO.getUserId());
        } else if (StringUtils.isBlank(request.getMemberId()) && StringUtils.isBlank(request.getAttachUserId())) {
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getUserId());
            AssertUtil.assertNotNull(userInfoBO, "?????????????????????");
            request.setMemberId(userInfoBO.getUserId());
        }
        // ??????????????????????????????
        OrganizationBO organizationBO = organizationManager.createOrganization(request);

        // ????????????????????????
        PermBO memberManagePerm = PermBOBuilder.getInstance(OrganizationPermType.ORG_MEMBER_MANAGE, organizationBO.getOrganizationName() + "_??????????????????").build();
        // ?????????id ?????????????????????
        memberManagePerm.putExtInfo(OrganizationPermExInfoKey.ORGANIZATION_ID, organizationBO.getOrganizationId());
        // ????????????
        PermManageRequest memberPermRequest = new PermManageRequest();
        memberPermRequest.setPermBO(memberManagePerm);
        memberManagePerm = permManager.createPerm(memberPermRequest);

        // ??????????????????????????????
        PermBO memberTypeManagePerm = PermBOBuilder.getInstance(OrganizationPermType.ORG_MEMBER_TYPE_MANAGE, organizationBO.getOrganizationName() + "_????????????????????????").build();
        // ?????????id ?????????????????????
        memberTypeManagePerm.putExtInfo(OrganizationPermExInfoKey.ORGANIZATION_ID, organizationBO.getOrganizationId());
        // ????????????
        PermManageRequest memberTypePermRequest = new PermManageRequest();
        memberTypePermRequest.setPermBO(memberTypeManagePerm);
        memberTypeManagePerm = permManager.createPerm(memberTypePermRequest);

        // ????????????????????????????????????
        if (StringUtils.isNotBlank(request.getMemberId())) {
            UserManageRequest principalPermRequest = new UserManageRequest();
            principalPermRequest.setPermIds(Arrays.asList(memberManagePerm.getPermId(), memberTypeManagePerm.getPermId()));
            principalPermRequest.setUserId(request.getMemberId());
            userManager.batchBindPerm(principalPermRequest);
        }

        // ?????????????????? ????????????????????????
        organizationBO.putExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM, memberManagePerm.getPermId());
        organizationBO.putExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM, memberTypeManagePerm.getPermId());
        organizationRepoService.modify(organizationBO);
        return organizationBO;
    }

    @Override
    public void update(OrganizationRequest request) {
        OrganizationBO organizationBO = new OrganizationBO();
        organizationRegisterValidator.validate(request);
        organizationBO.setOrganizationName(request.getOrganizationName());
        organizationBO.setOrganizationId(request.getOrganizationId());
        organizationRepoService.modify(organizationBO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void memberManage(OrganizationRequest request) {
        // ????????????
        if (StringUtils.isBlank(request.getMemberId())) {
            AssertUtil.assertStringNotBlank(request.getAttachUserId());
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getAttachUserId());
            AssertUtil.assertNotNull(userInfoBO, UserErrorCode.USER_NOT_EXIST);
            request.setMemberId(userInfoBO.getUserId());
        }

        // ????????????
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // type ??????
        AssertUtil.assertNotNull(request.getMemberType(), "??????????????????????????????");
        // ??????????????????id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        switch (request.getMemberType()) {
            case PRINCIPAL:
                // ????????????????????? ???????????????
                OrganizationMemberBO principal = organizationMemberRepoService.queryPrincipal(request.getOrganizationId());
                UserManageRequest principalPermRequest = new UserManageRequest();
                principalPermRequest.setPermIds(Arrays.asList(orgMemberManagePermId, orgMemberTypeManagePermId));

                // ?????????????????????
                principalPermRequest.setUserId(principal.getMemberId());
                userManager.batchUnbindPerm(principalPermRequest);
                // ????????????????????????
                principalPermRequest.setUserId(request.getMemberId());
                userManager.batchBindPerm(principalPermRequest);
                break;
            case ADMIN:
                // ?????????????????????
                UserManageRequest adminPermRequest = new UserManageRequest();
                adminPermRequest.setPermIds(Collections.singletonList(orgMemberManagePermId));
                adminPermRequest.setUserId(request.getMemberId());
                userManager.batchBindPerm(adminPermRequest);
                break;
            case MEMBER:
                // ???????????????
                break;
            default:
                throw new KaiChiException("??????????????????");
        }
        // ???????????????
        organizationManager.manageMember(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(OrganizationRequest request) {
        // ????????????
        if (StringUtils.isBlank(request.getMemberId())) {
            AssertUtil.assertStringNotBlank(request.getAttachUserId());
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getUserId());
            AssertUtil.assertNotNull(userInfoBO, UserErrorCode.USER_NOT_EXIST);
            request.setMemberId(userInfoBO.getUserId());
        }

        // ????????????
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // ??????????????????id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        OrganizationMemberBO member = organizationMemberRepoService.queryMember(request.getOrganizationId(), request.getMemberId());

        // ????????????
        if (member != null) {
            // ??????????????????
            UserManageRequest PermRequest = new UserManageRequest();
            PermRequest.setPermIds(Arrays.asList(orgMemberManagePermId, orgMemberTypeManagePermId));
            PermRequest.setUserId(member.getMemberId());
            userManager.batchUnbindPerm(PermRequest);

            organizationMemberRepoService.removeMember(request.getOrganizationId(), request.getMemberId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disband(OrganizationRequest request) {
        // ????????????
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // ??????????????????id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        // ????????????????????????
        permManager.detachAllUsers(orgMemberManagePermId);
        permManager.detachAllUsers(orgMemberTypeManagePermId);
        // ????????????
        permRepoService.deletePerm(orgMemberManagePermId);
        permRepoService.deletePerm(orgMemberTypeManagePermId);
        // ????????????
        organizationManager.disbandOrganization(request);
    }

    @Override
    public List<OrganizationMemberBO> queryOrganizationMemberByMemberId(OrganizationRequest request) {
        return organizationManager.queryOrganizationMemberByMemberId(request.getMemberId());
    }

    @Override
    public List<List<OrganizationMemberBO>> queryOrganizationMemberByOrganizationId(OrganizationRequest request) {
        List<List<OrganizationMemberBO>> organizationMemberBOS = new ArrayList<>(3);
        organizationMemberBOS.add(attachViewNameAndAvatar(
                organizationMemberRepoService.queryMembers(request.getOrganizationId(), MemberType.PRINCIPAL.getType())
        ));
        organizationMemberBOS.add(attachViewNameAndAvatar(
                organizationMemberRepoService.queryMembers(request.getOrganizationId(), MemberType.ADMIN.getType())
        ));
        organizationMemberBOS.add(attachViewNameAndAvatar(
                organizationMemberRepoService.queryMembers(request.getOrganizationId(), MemberType.MEMBER.getType())
        ));

        return organizationMemberBOS;
    }

    private List<OrganizationMemberBO> attachViewNameAndAvatar(List<OrganizationMemberBO> organizationMemberBOS) {
        for (OrganizationMemberBO organizationMemberBO : organizationMemberBOS) {
            Map<String, String> map = new HashMap<>(2);
            UserInfoBO userInfoBO = userBasicService.getByUserId(organizationMemberBO.getMemberId()).getUserInfo();
            map.put("mobilePhone", userInfoBO.getMobilePhone());
            if (userInfoBO.getRealNameCheck() == 1) {
                map.put("userViewName", userInfoBO.getRealName());
            } else if (userInfoBO.getNickname() != null) {
                map.put("userViewName", userInfoBO.getNickname());
            } else {
                map.put("userViewName", userInfoBO.getMobilePhone());
            }

            try {
                map.put("avatarUrl", obsUtil.getSignatureDownloadUrl(AVATAR_LOCATION, HashUtil.sha256(organizationMemberBO.getMemberId()), 120L));
            } catch (Exception ignored) {
            }
            organizationMemberBO.setExtInfo(map);
        }
        return organizationMemberBOS;
    }
}