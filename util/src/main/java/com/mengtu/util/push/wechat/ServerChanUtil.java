package com.mengtu.util.push.wechat;

import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * ServerChan 微信推送工具
 *
 * @author GHT
 * @version 1.1
 */
@Component
public class ServerChanUtil {

    /**
     * API 地址
     */
    private static final String BASE_URL = "https://sc.ftqq.com/";

    /**
     * 传递标识符
     */
    private static final String SEPARATOR = ".send";

    /**
     * 默认推送标题
     */
    private static final String DEFAULT_MESSAGE = "KaiChi推送通知";

    /**
     * 注入SCKEY
     */
    @Value("${kaichi.push.wechat.serverchan.sckey}")
    private String key;

    /**
     * 拼装获取完整接口URL
     *
     * @return String
     */
    private String getRequestUrl() {
        AssertUtil.assertStringNotBlank(key, "找不到或读取SCKEY异常");
        return BASE_URL + key + SEPARATOR;
    }

    /**
     * 一次推送
     *
     * @param message     推送标题
     * @param description 推送内容，请使用MarkDown语法
     */
    public JSONObject send(String message, String description) {
        Map<String, String> map = new HashMap<String, String>(2);
        map.put("text", message);
        map.put("desp", description);
        try {
            return JSONObject.parseObject(HttpUtil.doPost(this.getRequestUrl(), map));
        } catch (Exception e) {
            throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "ServerChan 请求异常");
        }
    }

    public JSONObject send(String description) {
        return this.send(DEFAULT_MESSAGE, description);
    }

}
