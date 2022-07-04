package com.mengtu.kaichi.user.model.basic;

import com.mengtu.util.common.ToString;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Map;

/**
 * 用户信息 BO
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:42
 */
public class UserInfoBO extends ToString {

    private static final long serialVersionUID = 1098716700943485968L;

    /**
     * 用户信息 ID
     */
    private String userInfoId;

    /**
     * 用户 ID
     */
    @NotBlank
    private String userId;

    /**
     * 注册时间
     */
    private Date registrationTime;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String mobilePhone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 实名认证校验标记
     */
    private Integer realNameCheck;

    /**
     * 拓展信息
     */
    private Map<String, String> extInfo;

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

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
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

    public Map<String, String> getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(Map<String, String> extInfo) {
        this.extInfo = extInfo;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
