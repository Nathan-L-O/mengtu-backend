package com.mengtu.kaichi.organization.model;

import com.mengtu.util.common.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 组织关系模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 14:46
 */
public class OrganizationRelationBO extends ToString {

    private static final long serialVersionUID = -710689548402070242L;

    /**
     * 组织关系id
     */
    private String organizationRelationId;

    /**
     * 主组织id
     */
    private String primaryOrganizationId;

    /**
     * 子组织id
     */
    private String subOrganizationId;

    private Map<String, String> extInfo = new HashMap<>();

    public String getOrganizationRelationId() {
        return organizationRelationId;
    }

    public void setOrganizationRelationId(String organizationRelationId) {
        this.organizationRelationId = organizationRelationId;
    }

    public String getPrimaryOrganizationId() {
        return primaryOrganizationId;
    }

    public void setPrimaryOrganizationId(String primaryOrganizationId) {
        this.primaryOrganizationId = primaryOrganizationId;
    }

    public String getSubOrganizationId() {
        return subOrganizationId;
    }

    public void setSubOrganizationId(String subOrganizationId) {
        this.subOrganizationId = subOrganizationId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}