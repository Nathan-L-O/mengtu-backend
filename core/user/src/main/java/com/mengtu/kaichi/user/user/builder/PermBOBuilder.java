package com.mengtu.kaichi.user.user.builder;

import com.mengtu.kaichi.user.model.basic.perm.PermBO;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限构建者
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 10:41
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class PermBOBuilder {

    /**
     * 权限类型
     */
    private String permType;

    /**
     * 权限名称
     */
    private String permName;

    /**
     * 权限描述
     */
    private String permDesc;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public static PermBOBuilder getInstance(String permType, String permName) {
        return new PermBOBuilder(permType, permName);
    }

    public static PermBOBuilder getInstance() {
        return new PermBOBuilder();
    }

    public PermBO build() {
        PermBO permBO = new PermBO();
        permBO.setPermType(permType);
        permBO.setPermName(permName);
        permBO.setPermDesc(permDesc);
        if (extInfo != null) {
            permBO.setExtInfo(extInfo);
        }
        return permBO;
    }

    private PermBOBuilder(String permType, String permName) {
        this.permType = permType;
        this.permName = permName;
    }

    private PermBOBuilder() {
    }

    public PermBOBuilder withPermType(String permType) {
        this.permType = permType;
        return this;
    }

    public PermBOBuilder withPermName(String permName) {
        this.permName = permName;
        return this;
    }

    public PermBOBuilder withPermDesc(String permDesc) {
        this.permDesc = permDesc;
        return this;
    }

    public PermBOBuilder withExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
        return this;
    }
}
