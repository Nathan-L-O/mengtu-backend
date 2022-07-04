package com.mengtu.kaichi.organization.dal.convert;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.organization.dal.model.OrganizationDO;
import com.mengtu.kaichi.organization.dal.model.OrganizationInvitationDO;
import com.mengtu.kaichi.organization.dal.model.OrganizationMemberDO;
import com.mengtu.kaichi.organization.dal.model.OrganizationRelationDO;
import com.mengtu.kaichi.organization.model.OrganizationBO;
import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.kaichi.organization.model.OrganizationMemberBO;
import com.mengtu.kaichi.organization.model.OrganizationRelationBO;

import java.util.Map;

/**
 * 实体转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:33
 */
final public class EntityConverter {

    /**
     * 组织DO2BO
     *
     * @param organizationDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static OrganizationBO convert(OrganizationDO organizationDO) {
        if (organizationDO == null) {
            return null;
        }
        OrganizationBO organizationBO = new OrganizationBO();
        organizationBO.setOrganizationId(organizationDO.getOrganizationId());
        organizationBO.setOrganizationName(organizationDO.getOrganizationName());
        organizationBO.setOrganizationType(organizationDO.getOrganizationType());
        organizationBO.setExtInfo(JSON.parseObject(organizationDO.getExtInfo(), Map.class));
        return organizationBO;
    }

    /**
     * 组织BO2DO
     *
     * @param organizationBO
     * @return
     */
    public static OrganizationDO convert(OrganizationBO organizationBO) {
        if (organizationBO == null) {
            return null;
        }
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrganizationId(organizationBO.getOrganizationId());
        organizationDO.setOrganizationName(organizationBO.getOrganizationName());
        organizationDO.setOrganizationType(organizationBO.getOrganizationType());
        organizationDO.setExtInfo(JSON.toJSONString(organizationBO.getExtInfo()));
        return organizationDO;
    }

    /**
     * 组织成员关系DO2BO
     *
     * @param memberDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static OrganizationMemberBO convert(OrganizationMemberDO memberDO) {
        if (memberDO == null) {
            return null;
        }
        OrganizationMemberBO memberBO = new OrganizationMemberBO();
        memberBO.setOrganizationMemberId(memberDO.getOrganizationMemberId());
        memberBO.setMemberId(memberDO.getMemberId());
        memberBO.setOrganizationId(memberDO.getOrganizationId());
        memberBO.setMemberType(memberDO.getMemberType());
        memberBO.setMemberDescription(memberDO.getMemberDescription());
        memberBO.setOrganizationName(memberDO.getOrganizationName());
        memberBO.setExtInfo(JSON.parseObject(memberDO.getExtInfo(), Map.class));
        return memberBO;
    }

    /**
     * 组织成员关系BO2DO
     *
     * @param memberBO
     * @return
     */
    public static OrganizationMemberDO convert(OrganizationMemberBO memberBO) {
        if (memberBO == null) {
            return null;
        }
        OrganizationMemberDO memberDO = new OrganizationMemberDO();
        memberDO.setOrganizationMemberId(memberBO.getOrganizationMemberId());
        memberDO.setMemberId(memberBO.getMemberId());
        memberDO.setOrganizationId(memberBO.getOrganizationId());
        memberDO.setOrganizationName(memberBO.getOrganizationName());
        memberDO.setMemberType(memberBO.getMemberType());
        memberDO.setMemberDescription(memberBO.getMemberDescription());
        memberDO.setExtInfo(JSON.toJSONString(memberBO.getExtInfo()));
        return memberDO;
    }

    /**
     * 组织关系DO2BO
     *
     * @param relationDO
     * @return
     */
    @SuppressWarnings("unchecked")
    public static OrganizationRelationBO convert(OrganizationRelationDO relationDO) {
        if (relationDO == null) {
            return null;
        }
        OrganizationRelationBO relationBO = new OrganizationRelationBO();
        relationBO.setOrganizationRelationId(relationDO.getOrganizationRelationId());
        relationBO.setPrimaryOrganizationId(relationDO.getPrimaryOrganizationId());
        relationBO.setSubOrganizationId(relationDO.getSubOrganizationId());
        relationBO.setExtInfo(JSON.parseObject(relationDO.getExtInfo(), Map.class));
        return relationBO;
    }

    /**
     * 组织关系BO2DO
     *
     * @param relationBO
     * @return
     */
    public static OrganizationRelationDO convert(OrganizationRelationBO relationBO) {
        if (relationBO == null) {
            return null;
        }
        OrganizationRelationDO relationDO = new OrganizationRelationDO();
        relationDO.setOrganizationRelationId(relationBO.getOrganizationRelationId());
        relationDO.setPrimaryOrganizationId(relationBO.getPrimaryOrganizationId());
        relationDO.setSubOrganizationId(relationBO.getSubOrganizationId());
        relationDO.setExtInfo(JSON.toJSONString(relationBO.getExtInfo()));
        return relationDO;
    }

    /**
     * 组织邀请BO2DO
     *
     * @param organizationInvitationBO
     * @return
     */
    public static OrganizationInvitationDO convert(OrganizationInvitationBO organizationInvitationBO) {
        if (organizationInvitationBO == null) {
            return null;
        }
        OrganizationInvitationDO organizationInvitationDO = new OrganizationInvitationDO();
        organizationInvitationDO.setOrganizationId(organizationInvitationBO.getOrganizationId());
        organizationInvitationDO.setOrganizationInvitationId(organizationInvitationBO.getOrganizationInvitationId());
        organizationInvitationDO.setMemberId(organizationInvitationBO.getMemberId());
        organizationInvitationDO.setLifetime(organizationInvitationBO.getLifetime());
        organizationInvitationDO.setEncodeUrl(organizationInvitationBO.getEncodeUrl());
        organizationInvitationDO.setExtInfo(JSON.toJSONString(organizationInvitationBO.getExtInfo()));
        return organizationInvitationDO;
    }

    /**
     * 组织邀请DO2BO
     *
     * @param organizationInvitationDO
     * @return
     */
    public static OrganizationInvitationBO convert(OrganizationInvitationDO organizationInvitationDO) {
        if (organizationInvitationDO == null) {
            return null;
        }
        OrganizationInvitationBO organizationInvitationBO = new OrganizationInvitationBO();
        organizationInvitationBO.setOrganizationId(organizationInvitationDO.getOrganizationId());
        organizationInvitationBO.setOrganizationInvitationId(organizationInvitationDO.getOrganizationInvitationId());
        organizationInvitationBO.setMemberId(organizationInvitationDO.getMemberId());
        organizationInvitationBO.setLifetime(organizationInvitationDO.getLifetime());
        organizationInvitationBO.setEncodeUrl(organizationInvitationDO.getEncodeUrl());
        organizationInvitationBO.setExtInfo(JSON.parseObject(organizationInvitationDO.getExtInfo(), Map.class));
        return organizationInvitationBO;
    }
}