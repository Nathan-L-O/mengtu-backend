package com.mengtu.kaichi.serviceimpl.organization.constant;

/**
 * 组织权限常量
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:34
 */
public class OrganizationPermType {

    //=================总管理权限 start=================

    /**
     * 所有组织管理权限 创建&删除
     */
    public final static String ALL_ORG_MANAGE = "ALL_ORG_MANAGE";

    /**
     * 所有组织关系管理
     */
    public final static String ALL_ORG_RELATION_MANAGE = "ALL_ORG_RELATION_MANAGE";

    /**
     * 所有组织成员管理
     */
    public final static String ALL_ORG_MEMBER_MANAGE = "ALL_ORG_MEMBER_MANAGE";

    //=================总管理权限 end=================

    /**
     * 组织成员管理 成员增加删除
     */
    public final static String ORG_MEMBER_MANAGE = "ORG_MEMBER_MANAGE";

    /**
     * 组织成员身份管理 分配主管 管理员
     */
    public final static String ORG_MEMBER_TYPE_MANAGE = "ORG_MEMBER_TYPE_MANAGE";


}