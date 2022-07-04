package com.mengtu.kaichi.user.request;

import com.mengtu.kaichi.user.model.basic.perm.RoleBO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色创建请求
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:36
 */
public class RoleManageRequest extends BaseRequest {

    private static final long serialVersionUID = -4538121672439886607L;

    /**
     * 权限实体
     */
    @NotNull
    private RoleBO role;


    /**
     * 绑定的用户
     */
    private List<String> userIds = new ArrayList<>();


    /**
     * 绑定的权限ids
     */
    private List<String> permIds = new ArrayList<>();

    public RoleBO getRole() {
        return role;
    }

    public void setRole(RoleBO role) {
        this.role = role;
    }

    public List<String> getPermIds() {
        return permIds;
    }

    public void setPermIds(List<String> permIds) {
        this.permIds = permIds;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
