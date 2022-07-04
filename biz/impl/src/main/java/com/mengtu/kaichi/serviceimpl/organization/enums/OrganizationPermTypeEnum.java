package com.mengtu.kaichi.serviceimpl.organization.enums;

import com.mengtu.kaichi.serviceimpl.organization.constant.OrganizationPermType;
import com.mengtu.kaichi.user.enums.PermType;
import org.apache.commons.lang.StringUtils;

/**
 * 权限活动类型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:34
 * @return null
 */
public enum OrganizationPermTypeEnum implements PermType {

    //=================总管理权限 start=================

    /**
     * 所有组织管理权限 创建&删除
     */
    ALL_ORG_MANAGE(OrganizationPermType.ALL_ORG_MANAGE, "全部组织管理", true),

    /**
     * 所有组织关系管理
     */
    ALL_ORG_RELATION_MANAGE(OrganizationPermType.ALL_ORG_RELATION_MANAGE, "全部组织关系管理", true),

    /**
     * 所有组织成员管理
     */
    ALL_ORG_MEMBER_MANAGE(OrganizationPermType.ALL_ORG_MEMBER_MANAGE, "全部成员管理", true),


    //=================总管理权限 end=================

    /**
     * 组织成员管理 成员 增加&删除
     */
    ORG_MEMBER_MANAGE(OrganizationPermType.ORG_MEMBER_MANAGE, "成员管理", false),


    /**
     * 组织成员身份管理 分配主管 管理员
     */
    ORG_MEMBER_TYPE_MANAGE(OrganizationPermType.ORG_MEMBER_TYPE_MANAGE, "成员身份管理", false);

    private String code;

    private String desc;

    private boolean init;

    public static OrganizationPermTypeEnum getByCode(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        for (OrganizationPermTypeEnum permType : values()) {
            if (StringUtils.equals(permType.getCode(), code)) {
                return permType;
            }
        }
        return null;
    }

    OrganizationPermTypeEnum(String code, String desc, boolean init) {
        this.code = code;
        this.desc = desc;
        this.init = init;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public boolean isInit() {
        return init;
    }
}
