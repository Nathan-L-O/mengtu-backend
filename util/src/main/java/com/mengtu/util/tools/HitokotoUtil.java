package com.mengtu.util.tools;

import com.alibaba.fastjson.JSONObject;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Hitokoto 工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 14:01
 */
public class HitokotoUtil {

    /**
     * API 地址
     */
    public static final String BASE_URL = "https://v1.hitokoto.cn/";

    /**
     * 句子类型（请求参数）
     */
    public static final String TYPE = "a";

    /**
     * 返回格式（请求参数）
     */
    public static final String ENCODE = "json";

    /**
     * 编码字符集（请求参数）
     */
    public static final String CHARSET = "utf-8";

    /**
     * 传递标识符
     */
    private static final String SEPARATOR = "?";

    /**
     * 拼装请求 URL
     *
     * @return requestUrl
     */
    public static String getRequestUrl() {
        String requestUrl = BASE_URL + SEPARATOR;

        Map<String, String> map = new HashMap<String, String>(3);
        map.put("c", TYPE);
        map.put("encode", ENCODE);
        map.put("charset", CHARSET);

        return requestUrl + MapUtil.toUrlString(map);
    }

    /**
     * 一言请求
     *
     * @return JSONObject
     */
    public static JSONObject get() {
        try {
            return JSONObject.parseObject(HttpUtil.doGet(getRequestUrl()));
        } catch (Exception e) {
            throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "一言请求异常");
        }
    }

    /**
     * 一次格式化控制台输出
     */
    public static void println() {
        JSONObject hitokoto = get();

        String printFormat = "\n一言:\t";
        printFormat += hitokoto.get("hitokoto") + "\t--";
        printFormat += hitokoto.get("from") + "\n";

        System.out.println(printFormat);
    }
}
