package com.mengtu.kaichi.model.user.vo;

import com.mengtu.kaichi.user.model.BasicUser;

import java.util.List;

/**
 * 用户页面模型
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:59
 */
@SuppressWarnings("AlibabaPojoMustOverrideToString")
public class UserVO extends BasicUser {

    private static final long serialVersionUID = -771078820620738989L;

    public List<String> jobInfo;

    private List<String> roleInfo;

    public List<String> getRoleInfo() {
        return roleInfo;
    }

    public void setRoleInfo(List<String> roleInfo) {
        this.roleInfo = roleInfo;
    }

    public List<String> getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(List<String> jobInfo) {
        this.jobInfo = jobInfo;
    }

}
