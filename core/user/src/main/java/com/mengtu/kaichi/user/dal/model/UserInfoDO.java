package com.mengtu.kaichi.user.dal.model;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户信息 DO
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:34
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_info",
        indexes = {
                @Index(name = "uk_user_info_id_user_id", columnList = "user_info_id, user_id", unique = true),
                @Index(name = "uk_user_id", columnList = "user_id", unique = true),
                @Index(name = "uk_user_info_id", columnList = "user_info_id", unique = true),
        })
public class UserInfoDO extends BaseDO {

    /**
     * 用户信息 ID
     */
    @Column(name = "user_info_id", length = 32, updatable = false, nullable = false)
    private String userInfoId;

    /**
     * 用户 ID
     */
    @Column(name = "user_id", length = 32, nullable = false)
    private String userId;

    /**
     * 姓名
     */
    @Column(name = "real_name", length = 32)
    private String realName;

    /**
     * 性别
     */
    @Column(length = 6)
    private String sex;

    /**
     * 手机号码
     */
    @Column(name = "mobile_phone", length = 32)
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    @Column(name = "email", length = 32)
    private String email;

    /**
     * 公司名称
     */
    @Column(name = "company", length = 32)
    private String company;

    /**
     * 生日
     */
    @Column(name = "birthday")
    private Date birthday;

    /**
     * 昵称
     */
    @Column(name = "nickname", length = 32)
    private String nickname;

    /**
     * 实名校验标记
     */
    @Column(name = "real_name_check", length = 2)
    private Integer realNameCheck;

    /**
     * 拓展信息
     */
    @Column(length = 2000)
    private String extInfo;

    public String getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(String userInfoId) {
        this.userInfoId = userInfoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getRealNameCheck() {
        return realNameCheck;
    }

    public void setRealNameCheck(Integer realNameCheck) {
        this.realNameCheck = realNameCheck;
    }

    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
