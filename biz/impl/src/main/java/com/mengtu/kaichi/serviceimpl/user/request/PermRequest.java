package com.mengtu.kaichi.serviceimpl.user.request;

import com.mengtu.kaichi.serviceimpl.common.verify.VerifyRequest;
import com.mengtu.kaichi.user.model.basic.perm.PermBO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 11:20
 */
public class PermRequest implements VerifyRequest {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 权限实体
     */
    private PermBO permBO;

    /**
     * 用户ids
     */
    private List<String> userIds;

    /**
     * 权限描述
     */
    private String permDescribe;

    /**
     * 权限id
     */
    private String permId;

    /**
     * 权限ids
     */
    private List<String> permIds;

    /**
     * 权限名字
     */
    private String permName;

    /**
     * 权限类型
     */
    private String permType;

    /**
     * 创建人id
     */
    private String creatorId;

    /**
     * 额外信息
     */
    private Map<String, String> extInfo;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    @Override
    public String getVerifyUserId() {
        return getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PermBO getPermBO() {
        return permBO;
    }

    public void setPermBO(PermBO permBO) {
        this.permBO = permBO;
    }

    public List<String> getStuIds() {
        return userIds;
    }

    public void setStuIds(List<String> stuIds) {
        this.userIds = stuIds;
    }

    public String getPermDescribe() {
        return permDescribe;
    }

    public void setPermDescribe(String permDescribe) {
        this.permDescribe = permDescribe;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public List<String> getPermIds() {
        return permIds;
    }

    public void setPermIds(List<String> permIds) {
        this.permIds = permIds;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    public String fetchExtInfo(String value) {
        if (value.equals("") || value == null) {
            return null;
        }
        return extInfo.get(value);
    }

    public void putExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }
}
