package com.mengtu.util.hash;

import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串数字摘要工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:51
 */
public class HashUtil {

    /**
     * Hash(MD5)
     *
     * @param input 输入字符串
     * @return
     */
    public static String md5(String input) {
        AssertUtil.assertStringNotBlank(input);
        char[] md5String = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] byteInput = input.getBytes();

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(byteInput);

            byte[] md5 = messageDigest.digest();

            int j = md5.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (byte byte0 : md5) {
                str[k++] = md5String[byte0 >>> 4 & 0xf];
                str[k++] = md5String[byte0 & 0xf];
            }

            return new String(str);

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Hash(SHA256)
     *
     * @param input 输入字符串
     * @return
     */
    public static String sha256(String input) {
        AssertUtil.assertStringNotBlank(input);
        MessageDigest messageDigest;
        String encodeString;

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(input.getBytes(StandardCharsets.UTF_8));
            encodeString = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new KaiChiException(e);
        }

        return encodeString.toUpperCase();
    }

    /**
     * Hash(SHA256)
     *
     * @param inputFile 输入文件
     * @return
     */
    public static String sha256(MultipartFile inputFile) {
        AssertUtil.assertNotNull(inputFile, CommonResultCode.ILLEGAL_PARAMETERS, "摘要文件不能为空");
        MessageDigest messageDigest;
        String encodeString;

        try {
            byte[] uploadBytes = inputFile.getBytes();
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] digest = messageDigest.digest(uploadBytes);
            encodeString = byte2Hex(digest);
        } catch (Exception e) {
            throw new KaiChiException(e);
        }
        return encodeString.toUpperCase();
    }

    /**
     * Hash(MD5)
     *
     * @param inputFile 输入文件
     * @return
     */
    public static String md5(MultipartFile inputFile) {
        AssertUtil.assertNotNull(inputFile, CommonResultCode.ILLEGAL_PARAMETERS, "摘要文件不能为空");
        MessageDigest messageDigest;
        String encodeString;

        try {
            byte[] uploadBytes = inputFile.getBytes();
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(uploadBytes);
            encodeString = byte2Hex(digest);
        } catch (Exception e) {
            throw new KaiChiException(e);
        }
        return encodeString.toUpperCase();
    }


    /**
     * 将byte转为16进制
     *
     * @param bytes 数组
     * @return
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }

}
