package com.mengtu.util.push.sms;

import com.alibaba.fastjson.JSONObject;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短信推送工具
 *
 * @author GHT
 * @version 1.1 @ 2020.04.29 10:44 PM
 */
@Component
public class MessageUtil {

    /**
     * 接口地址
     */
    private static final String API_URL = "https://api.wxxsxx.com/sms/send/mtsjhyhy";

    /**
     * 默认查询分页参数
     */
    private static final String DEFAULT_PER_PAGE = "200";

    /**
     * 注入接口授权 token
     */
    @Value("${kaichi.push.sms.token}")
    private String token;

    /**
     * 发送动作
     *
     * @param mobile       收信人手机号
     * @param variableList 变量表
     */
    public JSONObject send(String mobile, List<String> variableList) {
        Map<String, String> headerMap = new HashMap<>(1);

        String smsContent = String.format(
                "【梦途科技】您正在%s, 验证码为%s，%s分钟内有效。如非本人操作请忽略。",
                variableList.get(0), variableList.get(1), variableList.get(2));

        JSONObject bodyContent = new JSONObject();
        bodyContent.put("content", smsContent);
        bodyContent.put("mobile", mobile);
        String currentTime = String.valueOf(System.currentTimeMillis());

        headerMap.put("Authorization", String.format("HMAC-SHA256 %s,%s",
                currentTime, signature(bodyContent.toJSONString() + currentTime)));
        headerMap.put("Content-Type", "application/json");

        try {
            return JSONObject.parseObject(HttpUtil.doPost(API_URL, null, bodyContent, headerMap));
        } catch (Exception e) {
            throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "短信接口异常");
        }
    }

    private String signature(String data) {
        AssertUtil.assertStringNotBlank(token, "找不到或读取短信接口 token 异常");

        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            sha256Hmac.init(new SecretKeySpec(token.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getEncoder().encodeToString(
                    sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            throw new KaiChiException(RestResultCode.ILLEGAL_PARAMETERS, "短信接口签名异常");
        }
    }

}
