package com.mengtu.util.baidu;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * IP 地址及运营商查询工具（使用百度非公共API）
 *
 * @author GHT
 * @version 1.1
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Component
public class IPLocationQueryUtil {
    /**
     * API 地址
     */
    private static final String BASE_URL = "https://sp0.baidu.com/";

    /**
     * 传递标识符
     */
    private static final String SEPARATOR = "/api.php";

    /**
     * 默认请求参数（请求资源标识）
     */
    private static final String DEFAULT_RESOURCE_ID = "6006";

    /**
     * 默认请求参数（编码格式）
     */
    private static final String DEFAULT_CHARSET = "utf8";

    /**
     * 默认请求参数（数据格式）
     */
    private static final String DEFAULT_DATATYPE = "json";

    /**
     * 注入接口标识
     */
    @Value("${kaichi.util.baidu.ip-util.apikey}")
    private String key;

    /**
     * 拼装获取完整接口URL
     *
     * @return String
     */
    private String getRequestUrl() {
        AssertUtil.assertStringNotBlank(key, "找不到或读取apikey异常");
        return BASE_URL + key + SEPARATOR;
    }

    /**
     * 一次查询
     *
     * @param ip 要查询的 IP
     */
    public String query(String ip) {
        Map<String, String> map = new HashMap<>(5);
        map.put("resource_id", DEFAULT_RESOURCE_ID);
        map.put("ie", DEFAULT_CHARSET);
        map.put("oe", DEFAULT_CHARSET);
        map.put("format", DEFAULT_DATATYPE);
        map.put("query", ip);
        try {
            return HttpUtil.doPost(this.getRequestUrl(), map);
        } catch (Exception e) {
            throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "百度接口请求异常");
        }
    }

    /**
     * 反序列化 query() 查询结果并取出地址信息
     */
    public String getLocation(String ip) {
        JSONArray queryResult = JSONObject.parseObject(query(ip)).getJSONArray("data");
        AssertUtil.assertTrue(!queryResult.isEmpty());
        return queryResult.getJSONObject(0).getString("location");
    }

}
