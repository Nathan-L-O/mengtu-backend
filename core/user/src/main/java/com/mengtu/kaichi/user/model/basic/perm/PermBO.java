package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:37
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class PermBO extends ToString {

    private static final long serialVersionUID = -3306690079826272513L;
    
    /**
     * 权限 ID
     */
    private String permId;

    /**
     * 权限类型
     */
    @NotBlank
    private String permType;

    /**
     * 权限名称
     */
    @NotBlank
    private String permName;

    /**
     * 权限描述
     */
    private String permDesc;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo = new HashMap<>();

    /**
     * 放入拓展信息
     *
     * @param key
     * @param value
     */
    public void putExtInfo(String key, String value) {
        if (extInfo == null) {
            extInfo = new HashMap<>();
        }
        extInfo.put(key, value);
    }

    /**
     * 取出拓展信息
     *
     * @param key
     * @return
     */
    public String fetchExtInfo(String key) {
        if (extInfo == null) {
            return null;
        }
        return extInfo.get(key);
    }


    public String getPermId() {
        return permId;
    }

    public void setPermId(String permId) {
        this.permId = permId;
    }

    public String getPermType() {
        return permType;
    }

    public void setPermType(String permType) {
        this.permType = permType;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getPermDesc() {
        return permDesc;
    }

    public void setPermDesc(String permDesc) {
        this.permDesc = permDesc;
    }

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }
}
