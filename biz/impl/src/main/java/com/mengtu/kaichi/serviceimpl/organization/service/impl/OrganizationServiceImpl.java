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
 * 组织管理服务实现
 *
 * @author 过昊天
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
     * 头像 OBS 地址
     */
    private static final String AVATAR_LOCATION = "user/avatar/";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrganizationBO create(OrganizationRequest request) {
        organizationRegisterValidator.validate(request);
        if (StringUtils.isBlank(request.getMemberId()) && StringUtils.isNotBlank(request.getAttachUserId())) {
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getAttachUserId());
            AssertUtil.assertNotNull(userInfoBO, "组织指定主管用户不存在");
            request.setMemberId(userInfoBO.getUserId());
        } else if (StringUtils.isBlank(request.getMemberId()) && StringUtils.isBlank(request.getAttachUserId())) {
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getUserId());
            AssertUtil.assertNotNull(userInfoBO, "主管用户不存在");
            request.setMemberId(userInfoBO.getUserId());
        }
        // 创建组织领域相关信息
        OrganizationBO organizationBO = organizationManager.createOrganization(request);

        // 创建成员管理权限
        PermBO memberManagePerm = PermBOBuilder.getInstance(OrganizationPermType.ORG_MEMBER_MANAGE, organizationBO.getOrganizationName() + "_成员管理权限").build();
        // 将组织id 保存到拓展信息
        memberManagePerm.putExtInfo(OrganizationPermExInfoKey.ORGANIZATION_ID, organizationBO.getOrganizationId());
        // 构建请求
        PermManageRequest memberPermRequest = new PermManageRequest();
        memberPermRequest.setPermBO(memberManagePerm);
        memberManagePerm = permManager.createPerm(memberPermRequest);

        // 创建成员身份管理权限
        PermBO memberTypeManagePerm = PermBOBuilder.getInstance(OrganizationPermType.ORG_MEMBER_TYPE_MANAGE, organizationBO.getOrganizationName() + "_成员身份管理权限").build();
        // 将组织id 保存到拓展信息
        memberTypeManagePerm.putExtInfo(OrganizationPermExInfoKey.ORGANIZATION_ID, organizationBO.getOrganizationId());
        // 构建请求
        PermManageRequest memberTypePermRequest = new PermManageRequest();
        memberTypePermRequest.setPermBO(memberTypeManagePerm);
        memberTypeManagePerm = permManager.createPerm(memberTypePermRequest);

        // 如果指定主管直接绑定权限
        if (StringUtils.isNotBlank(request.getMemberId())) {
            UserManageRequest principalPermRequest = new UserManageRequest();
            principalPermRequest.setPermIds(Arrays.asList(memberManagePerm.getPermId(), memberTypeManagePerm.getPermId()));
            principalPermRequest.setUserId(request.getMemberId());
            userManager.batchBindPerm(principalPermRequest);
        }

        // 更新组织信息 拓展信息添加权限
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
        // 验证用户
        if (StringUtils.isBlank(request.getMemberId())) {
            AssertUtil.assertStringNotBlank(request.getAttachUserId());
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getAttachUserId());
            AssertUtil.assertNotNull(userInfoBO, UserErrorCode.USER_NOT_EXIST);
            request.setMemberId(userInfoBO.getUserId());
        }

        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // type 分发
        AssertUtil.assertNotNull(request.getMemberType(), "管理成员时未指定身份");
        // 组织对应权限id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        switch (request.getMemberType()) {
            case PRINCIPAL:
                // 移除原主管权限 赋予新主管
                OrganizationMemberBO principal = organizationMemberRepoService.queryPrincipal(request.getOrganizationId());
                UserManageRequest principalPermRequest = new UserManageRequest();
                principalPermRequest.setPermIds(Arrays.asList(orgMemberManagePermId, orgMemberTypeManagePermId));

                // 移除原主管权限
                principalPermRequest.setUserId(principal.getMemberId());
                userManager.batchUnbindPerm(principalPermRequest);
                // 给新主管添加权限
                principalPermRequest.setUserId(request.getMemberId());
                userManager.batchBindPerm(principalPermRequest);
                break;
            case ADMIN:
                // 赋予管理员权限
                UserManageRequest adminPermRequest = new UserManageRequest();
                adminPermRequest.setPermIds(Collections.singletonList(orgMemberManagePermId));
                adminPermRequest.setUserId(request.getMemberId());
                userManager.batchBindPerm(adminPermRequest);
                break;
            case MEMBER:
                // 无权限操作
                break;
            default:
                throw new KaiChiException("非法成员类型");
        }
        // 处理成员表
        organizationManager.manageMember(request);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMember(OrganizationRequest request) {
        // 验证用户
        if (StringUtils.isBlank(request.getMemberId())) {
            AssertUtil.assertStringNotBlank(request.getAttachUserId());
            UserInfoBO userInfoBO = userInfoRepoService.queryUserInfoByUserId(request.getUserId());
            AssertUtil.assertNotNull(userInfoBO, UserErrorCode.USER_NOT_EXIST);
            request.setMemberId(userInfoBO.getUserId());
        }

        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // 组织对应权限id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        OrganizationMemberBO member = organizationMemberRepoService.queryMember(request.getOrganizationId(), request.getMemberId());

        // 成员存在
        if (member != null) {
            // 移除所有权限
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
        // 验证组织
        OrganizationBO organizationBO = organizationRepoService.queryByOrganizationId(request.getOrganizationId());
        AssertUtil.assertNotNull(organizationBO, OrganizationErrorCode.ORGANIZATION_NOT_EXIST);

        // 组织对应权限id
        String orgMemberManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_MANAGE_PERM);
        String orgMemberTypeManagePermId = organizationBO.fetchExtInfo(OrganizationExtInfoKey.ORG_MEMBER_TYPE_MANAGE_PERM);

        // 跟所有人解绑权限
        permManager.detachAllUsers(orgMemberManagePermId);
        permManager.detachAllUsers(orgMemberTypeManagePermId);
        // 删除权限
        permRepoService.deletePerm(orgMemberManagePermId);
        permRepoService.deletePerm(orgMemberTypeManagePermId);
        // 删除组织
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