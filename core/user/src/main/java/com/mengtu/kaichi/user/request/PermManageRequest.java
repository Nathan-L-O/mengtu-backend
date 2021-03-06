package com.mengtu.kaichi.user.request;

import com.mengtu.kaichi.user.model.basic.perm.PermBO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限创建请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:35
 */
public class PermManageRequest extends BaseRequest {

    private static final long serialVersionUID = -8190025214504041091L;

    /**
     * 权限实体
     */
    @NotNull
    private PermBO permBO;

    /**
     * 绑定的用户
     */
    private List<String> userIds = new ArrayList<>();


    /**
     * 绑定的角色ids
     */
    private List<String> roleIds = new ArrayList<>();

    public PermBO getPermBO() {
        return permBO;
    }

    public void setPermBO(PermBO permBO) {
        this.permBO = permBO;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userId) {
        this.userIds = userId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPermId() {
        if (permBO != null) {
            return permBO.getPermId();
        }
        return null;
    }

    public void setPermId(String permId) {
        if (permBO == null) {
            permBO = new PermBO();
        }
        permBO.setPermId(permId);
    }
}
