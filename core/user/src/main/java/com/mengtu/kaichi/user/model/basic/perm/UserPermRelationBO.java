package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户权限关联映射
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:42
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class UserPermRelationBO extends ToString {

    private static final long serialVersionUID = 4032180651565769837L;

    /**
     * 用户权限映射 ID
     */
    private String userPermId;

    /**
     * 用户 ID
     */
    @NotBlank
    private String userId;

    /**
     * 权限 ID
     */
    @NotBlank
    private String permId;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    public String getUserPermId() {
        return userPermId;
    }

    public void setUserPermId(String userPermId) {
        this.userPermId = userPermId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }
}
