package com.mengtu.kaichi.serviceimpl.organization.service.impl;

import com.mengtu.kaichi.organization.dal.service.OrganizationMemberRepoService;
import com.mengtu.kaichi.organization.dal.service.OrganizationRepoService;
import com.mengtu.kaichi.organization.enums.MemberType;
import com.mengtu.kaichi.organization.enums.OrganizationErrorCode;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.serviceimpl.common.verify.VerifyPerm;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationExtInfoKey;
import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermType;
import com.mengtu.kaichi.serviceimpl.organization.request.OrganizationRequest;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationManagerService;
import com.mengtu.kaichi.serviceimpl.organization.service.OrganizationService;
import com.mengtu.kaichi.user.dal.service.UserInfoRepoService;
import com.mengtu.kaichi.user.enums.UserErrorCode;
import com.mengtu.kaichi.user.model.basic.UserInfoBO;
import com.mengtu.kaichi.user.user.service.UserBasicService;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.Collections;

/**
 * 组织管理器实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:49
 */
@Service
public class OrganizationManagerServiceImpl implements OrganizationManagerService {

    private static final String AVATAR_LOCATION = "organization/avatar/";

    @Resource
    private OrganizationService organizationService;

    @Resource
    private OrganizationRepoService organizationRepoService;

    @Resource
    private OrganizationMemberRepoService organizationMemberRepoService;

    @Resource
    private UserBasicService userBasicService;

    @Resource
    private UserInfoRepoService userInfoRepoService;

    @Resource
    private ObsUtil obsUtil;

    @Override
    public OrganizationBO create(OrganizationRequest request) {
        OrganizationBO organizationBO = organizationService.create(request);
        String hash = HashUtil.sha256(organizationBO.getOrganizationId());
        try {
            obsUtil.copy(obsUtil.fetchFile(AVATAR_LOCATION, "defaultOrganizationAvatar"),
                    AVATAR_LOCATION + hash + ".png");
        } catch (Exception ignored) {
        }
        return organizationBO;
    }

    @Override
    @VerifyPerm(permType = OrganizationPermType.ALL_ORG_MANAGE)
    public void disband(OrganizationRequest request) {
        organizationService.disband(request);
    }

    @Override
    @VerifyPerm(permType = OrganizationPermType.ALL_ORG_MEMBER_MANAGE)
    public void manageMember(OrganizationRequest request) {
        organizationService.memberManage(request);
    }

    @Override
    public void changeMemberType(OrganizationRequest request) {
        AssertUtil.assertTrue(verifyMemberTypePerm(request), CommonResultCode.FORBIDDEN, "没有该组织的身份管理权限");
        organizationService.memberManage(request);
    }

    @Override
    public void addMember(OrganizationRequest request) {
        // 防止未填充
        if (request.getMemberType() == null) {
            request.setMemberType(MemberType.MEMBER);
        }
        // 如果是只是添加成员 校验成员管理权限
        if (MemberType.MEMBER == request.getMemberType()) {
            AssertUtil.assertTrue(verifyMemberPerm(request), CommonResultCode.FORBIDDEN, "没有该组织的成员管理权限");
        } else {
            AssertUtil.assertTrue(verifyMemberTypePerm(request), CommonResultCode.FORBIDDEN, "没有该组织的身份管理权限");
        }
        organizationService.memberManage(request);
    }

    @Override
    public void removeMember(OrganizationRequest request) {
        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // 验证用户
        if (StringUtils.isBlank(request.getMemberId())) {
            AssertUtil.assertStringNotBlank(request.getAttachUserId());
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getAttachUserId());
            AssertUtil.assertNotNull(userInfoBO, UserErrorCode.USER_NOT_EXIST);
            request.setMemberId(userInfoBO.getUserId());
        }
        OrganizationMemberBO member = organizationMemberRepoService.queryMember(request.getOrganizationId(), request.getMemberId());

        // 存在用户则移除
        if (member != null) {
            MemberType memberType = MemberType.getByType(member.getMemberType());
            AssertUtil.assertNotNull(memberType, "管理用户类型为空, 请联系管理员");
            // 如果是只是添加成员 校验成员管理权限
            if (MemberType.MEMBER == request.getMemberType()) {
                AssertUtil.assertTrue(verifyMemberPerm(request), CommonResultCode.FORBIDDEN, "没有该组织的成员管理权限");
            } else {
                AssertUtil.assertTrue(verifyMemberTypePerm(request), CommonResultCode.FORBIDDEN, "没有该组织的身份管理权限");
            }
            organizationService.removeMember(request);
        }
    }

    @Override
    public void avatarUpdate(OrganizationRequest request, File avatarFile) {
        String hash = HashUtil.sha256(request.getOrganizationId());
        String originalFileName = avatarFile.getName();
        try {
            obsUtil.deleteFile(AVATAR_LOCATION, hash);
        } catch (Exception ignored) {
        }
        obsUtil.uploadFile(avatarFile, AVATAR_LOCATION, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));

    }

    @Override
    public void updateInfo(OrganizationRequest request) {
        organizationService.update(request);
    }

    /**
     * 校验成员管理权限
     *
     * @param request
     * @return
     */
    private boolean verifyMemberPerm(OrganizationRequest request) {
        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);
        // 获取权限
        String memberPermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        AssertUtil.assertStringNotBlank(memberPermId, "组织没有成员管理权限");

        return userBasicService.verifyPermissionByPermId(request.getVerifyUserId(), Collections.singletonList(memberPermId));
    }

    /**
     * 校验身份管理权限
     *
     * @param request
     * @return
     */
    private boolean verifyMemberTypePerm(OrganizationRequest request) {
        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);
        // 获取权限
        String memberTypePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);
        AssertUtil.assertStringNotBlank(memberTypePermId, "组织没有成员身份管理权限");

        return userBasicService.verifyPermissionByPermId(request.getVerifyUserId(), Collections.singletonList(memberTypePermId));
    }
}