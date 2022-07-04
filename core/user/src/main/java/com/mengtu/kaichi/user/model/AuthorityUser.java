package com.mengtu.kaichi.user.model;

import com.mengtu.kaichi.user.model.basic.perm.PermBO;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限用户模型(带权限)
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:42
 */
public class AuthorityUser extends CommonUser {

    private static final long serialVersionUID = -3037862938614559654L;

    /**
     * 权限信息
     * key permBO.getPermId()
     */
    private Map<String, PermBO> permission = new HashMap<>();


    /**
     * 放入权限
     *
     * @param perm
     */
    public void putPerm(PermBO perm) {
        if (permission == null) {
            permission = new HashMap<>();
        }
        permission.put(perm.getPermId(), perm);
    }

    /**
     * 获取权限
     *
     * @param permId
     * @return
     */
    public PermBO fetchPerm(String permId) {
        if (permission == null) {
            return null;
        }
        return permission.get(permId);
    }


    public Map<String, PermBO> getPermission() {
        return permission;
    }

    public void setPermission(Map<String, PermBO> permission) {
        this.permission = permission;
    }
}
