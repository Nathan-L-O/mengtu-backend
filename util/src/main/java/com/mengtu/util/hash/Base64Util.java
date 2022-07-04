package com.mengtu.util.hash;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Base 64 编码工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/29 22:54
 */
public class Base64Util {

    /**
     * 对给定的字符串进行 base64 解码操作
     */
    public static String decodeData(String inputData) {
        if (null == inputData) {
            return null;
        }
        return new String(Base64.decodeBase64(inputData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }

    /**
     * 对给定的字符串进行 base64 编码操作
     */
    public static String encodeData(String inputData) {
        if (null == inputData) {
            return null;
        }
        return new String(Base64.encodeBase64(inputData.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
    }
}