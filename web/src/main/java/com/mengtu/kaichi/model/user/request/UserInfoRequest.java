package com.mengtu.kaichi.model.user.request;

/**
 * 用户信息请求
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/25 13:53
 */
public class UserInfoRequest extends UserRequestBase {

    private static final long serialVersionUID = 1419114030136853712L;

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
    private String birthday;

    /**
     * 实名认证校验标记
     */
    private Integer realNameCheck;

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getRealNameCheck() {
        return realNameCheck;
    }

    public void setRealNameCheck(Integer realNameCheck) {
        this.realNameCheck = realNameCheck;
    }

    @Override
    public String toAuditString() {
        return super.toAuditString() + realName + sex + company;
    }
}
