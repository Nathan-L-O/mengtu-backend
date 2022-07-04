package com.mengtu.kaichi.user.dal.model.perm;

import com.mengtu.kaichi.user.dal.model.BaseDO;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户 DO
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:33
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user",
        indexes = {
                @Index(name = "uk_user_id", columnList = "user_id", unique = true),
                @Index(name = "uk_user_name", columnList = "user_name", unique = true),
                @Index(name = "uk_session_id", columnList = "session_id", unique = true)
        })
public class UserDO extends BaseDO {

    /**
     * userId 生成 不可修改
     */
    @Column(name = "user_id", length = 32, nullable = false, updatable = false)
    private String userId;

    /**
     * 用户名
     */
    @Column(name = "user_name", length = 32, nullable = false)
    private String username;

    /**
     * 密码 长度不超过32位
     */
    @Column(length = 32, nullable = false)
    private String password;

    /**
     * 盐
     */
    @Column(length = 36, nullable = false)
    private String salt;

    /**
     * 维持登陆态 sessionId
     */
    @Column(name = "session_id", length = 32)
    private String sessionId;

    /**
     * 上次登录时间
     */
    @Column
    private Date lastLoginDate;

    /**
     * 上次登录ip
     */
    @Column(name = "last_login_ip", length = 32)
    private String lastLoginIP;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
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

