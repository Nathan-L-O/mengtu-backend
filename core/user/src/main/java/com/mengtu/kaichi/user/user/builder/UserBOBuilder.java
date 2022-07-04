package com.mengtu.kaichi.user.user.builder;

import org.apache.commons.lang.StringUtils;
import com.mengtu.kaichi.user.model.basic.perm.UserBO;
import com.mengtu.kaichi.user.util.EncryptUtil;

import java.util.UUID;

/**
 * 用户构建器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/22 15:36
 */
final public class UserBOBuilder {

    /**
     * 用户名
     */
    private String username;

    /**
     * 明文密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 传入基本参数可以构建用户
     *
     * @param username
     * @param password
     * @return
     */
    public static UserBOBuilder getInstance(String username, String password) {
        return new UserBOBuilder(username, password);
    }

    public UserBO build() {
        UserBO userBO = new UserBO();
        // 设置盐，没有指定盐就用uuid
        if (StringUtils.isNotBlank(salt)) {
            userBO.setSalt(salt);
        } else {
            userBO.setSalt(UUID.randomUUID().toString());
        }

        userBO.setUserName(username);
        userBO.setPassword(EncryptUtil.encryptPassword(password, userBO.getSalt()));
        return userBO;
    }


    /**
     * 构造函数
     *
     * @param username
     * @param password
     */
    private UserBOBuilder(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserBOBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBOBuilder withPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBOBuilder withSalt(String salt) {
        this.salt = salt;
        return this;
    }

}
