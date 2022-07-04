package com.mengtu.kaichi.user.util;

import com.mengtu.util.hash.HashUtil;

/**
 * 加密工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:45
 */
public class EncryptUtil {

    /**
     * 密码 md5 加盐处理 (hash(hash(pwd)+salt))
     *
     * @param password
     * @param salt
     * @return
     */
    public static String encryptPassword(String password, String salt) {
        return HashUtil.md5(HashUtil.md5(password) + salt);
    }

    /**
     * 解析 token 获得 sessionId
     *
     * @param tokenId
     * @return
     */
    public static String parseToken(String tokenId) {
        return HashUtil.md5(tokenId);
    }

    /**
     * 获取 token
     *
     * @param bizStr
     * @return
     */
    public static String getToken(String bizStr) {
        return HashUtil.md5(bizStr);
    }

}
