package com.mengtu.util.tools;

import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 字符编码工具类
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:49
 */
public class CharsetEncodingUtil {

    private static final Charset GBK = Charset.forName("GBK");
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * 是否可以转换成GBK编码
     *
     * @param str
     * @return
     */
    public static boolean canEncodeGBK(String str) {
        return StringUtils.isNotBlank(str) && GBK.newEncoder().canEncode(str);
    }

    /**
     * 是否可以转换成UTF-8编码
     *
     * @param str
     * @return
     */
    public static boolean canEncodeUTF8(String str) {
        return StringUtils.isNotBlank(str) && UTF_8.newEncoder().canEncode(str);
    }
}
