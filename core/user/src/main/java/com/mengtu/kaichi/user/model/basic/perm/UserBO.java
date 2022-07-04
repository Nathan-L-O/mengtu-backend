package com.mengtu.kaichi.user.model.basic.perm;

import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

/**
 * 用户模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:41
 */
public class UserBO extends ToString {

    private static final long serialVersionUID = 8457715075803728006L;

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    @NotBlank
    private String userName;

    /**
     * 密文密码
     */
    @NotBlank
    private String password;

    /**
     * 盐
     */
    @NotBlank
    private String salt;

    /**
     * 上次登陆 IP
     */
    private String lastLoginIp;

    /**
     * 上次登陆时间
     */
    private Date lastLoginDate;

    /**
     * 会话 ID
     */
    private String sessionId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
