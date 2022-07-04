package com.mengtu.util.verification;

import com.mengtu.util.enums.CaptchaType;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.push.sms.MessageUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.verification.model.UserVerificationCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 验证码生成及校验工具
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:55
 */
@Component
public class VerificationCodeUtil {

    /**
     * 验证码长度
     */
    private static final int CODE_LENGTH = 4;

    /**
     * 数字验证码长度
     */
    private static final int NUM_CODE_LENGTH = 6;

    /**
     * 计划任务 Quartz Cron 表达式 : CleanCycle
     */
    private static final String CORN_STRING_CLEAN = "0 */1 * * * ?";

    /**
     * 计划任务 Quartz Cron 表达式 : Control
     */
    private static final String CORN_STRING_CONTROL = "0 0 12 * * ?";

    /**
     * 验证码容器
     */
    private static final Map<String, UserVerificationCode> V_POOL = new ConcurrentHashMap<>();

    /**
     * 校验容器
     */
    private static final Map<String, Long> P_POOL = new ConcurrentHashMap<>();

    /**
     * 控制容器
     */
    private static final Map<String, Integer> C_POOL = new ConcurrentHashMap<>();

    /**
     * 校验有效期
     */
    private static final int PERIOD_OF_VALIDITY = 60;

    /**
     * 干扰线数量
     */
    private static final int NOISE_LINE_NUMBER = 6;

    /**
     * 干扰点数量
     */
    private static final int NOISE_DOT_NUMBER = 30;

    /**
     * 短信验证码频率控制间隔
     */
    private static final int FREQ_FLAG = 120;

    /**
     * 同一号码单日最大发送上限
     */
    private static final int MAX = 10;

    /**
     * 纯数字正则
     */
    private static final String NUM_CODE_REGEX = "[0-9]+";

    /**
     * 手机号正则
     */
    private static final String MOBILE_PHONE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";


    /**
     * 注入验证码生命周期
     */
    @Value("${kaichi.util.verificationcode.lifetime}")
    private int lifetime;

    @Resource
    private MessageUtil messageUtil;

    /**
     * 生成随机（字母+数字）验证码
     *
     * @return
     */
    public static List<String> getCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < CODE_LENGTH; i++) {
            if (random.nextInt(2) % 2 == 0) {
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                int offset = random.nextInt(26);
                code.append((char) (offset == 14 ? offset + 1 + temp : offset + temp));
            } else {
                code.append(random.nextInt(9) + 1);
            }
        }

        List<String> codeObject = new ArrayList<>();
        codeObject.add(code.toString());

        return codeObject;
    }

    /**
     * 生成随机（数字）验证码
     *
     * @return
     */
    public static List<String> getNumCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < NUM_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }

        List<String> codeObject = new ArrayList<>();
        codeObject.add(code.toString());

        return codeObject;
    }

    /**
     * 生成表达式验证码
     *
     * @return
     */
    public static List<String> getExpression() {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        int a, b, result;

        if (random.nextInt(2) % 2 == 0) {
            a = random.nextInt(9) + 1;
            b = random.nextInt(9) + 1;
            code.append(a);
            code.append("*");
            code.append(b);
            code.append("=");
            result = a * b;
        } else if (random.nextInt(2) % 2 == 0) {
            a = random.nextInt(9) + 1;
            b = random.nextInt(9) + 1;
            code.append(a);
            code.append("+");
            code.append(b);
            code.append("=");
            result = a + b;
        } else if (random.nextInt(2) % 2 == 0) {
            a = random.nextInt(5) + 5;
            b = random.nextInt(5);
            code.append(a);
            code.append("-");
            code.append(b);
            code.append("=");
            result = a - b;
        } else {
            a = (random.nextInt(4) + 1) * 2;
            code.append(a);
            code.append("÷");
            code.append(2);
            code.append("=");
            result = a / 2;
        }

        List<String> codeObject = new ArrayList<>(2);
        codeObject.add(code.toString());
        codeObject.add(String.valueOf(result));

        return codeObject;
    }

    /**
     * 生成验证码并返回
     *
     * @param userId UserID
     * @return code
     */
    public static String initCode(String userId, CaptchaType captchaType) {
        AssertUtil.assertStringNotBlank(userId);
        UserVerificationCode userVerificationCode = new UserVerificationCode(userId, captchaType);
        V_POOL.put(userId, userVerificationCode);
        return userVerificationCode.getCode();
    }

    /**
     * 生成（字母+数字）验证码并写入 BufferedImage
     *
     * @param userId UserID
     * @param image  带缓冲图像
     * @param width  宽度
     * @param height 高度
     */
    public static void initCode(String userId, BufferedImage image, int width, int height) {
        String code = initCode(userId, CaptchaType.EXPRESSION);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        Random random = new Random();
        int x = 10;

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setFont(new Font("en_US.UTF-8", Font.BOLD, 40));

        for (int i = 0; i < code.length(); i++) {
            graphics.setColor(getRandomColor());
            int degree = random.nextInt() % 30;
            graphics.rotate(degree * Math.PI / 180, x, 45);
            graphics.drawString(String.valueOf(code.charAt(i)), x, 45);
            graphics.rotate(-degree * Math.PI / 180, x, 45);
            x += 48;
        }
        for (int i = 0; i < NOISE_LINE_NUMBER; i++) {
            graphics.setColor(getRandomColor());
            graphics.drawLine(random.nextInt(width), random.nextInt(height), random.nextInt(width), random.nextInt(height));
        }
        for (int i = 0; i < NOISE_DOT_NUMBER; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            graphics.setColor(getRandomColor());
            graphics.fillRect(x1, y1, 2, 2);
        }
    }

    /**
     * 随机取色
     */
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }

    /**
     * 短信验证码请求频率控制
     */
    private static void frequencyControl(String userId) {
        UserVerificationCode userVerificationCode = V_POOL.get(userId);
        if (userVerificationCode != null && userVerificationCode.getCode().matches(NUM_CODE_REGEX)) {
            long timeDifference = System.currentTimeMillis() / 1000 - userVerificationCode.getTimestamp();
            AssertUtil.assertTrue(timeDifference >= FREQ_FLAG, CommonResultCode.SYSTEM_ERROR, "操作太频繁啦，请" + (FREQ_FLAG - timeDifference) + "秒后再试");
        }
    }

    /**
     * 校验，一次有效
     */
    public static void validate(String userId, String code) {
        AssertUtil.assertStringNotBlank(code);
        AssertUtil.assertTrue(V_POOL.containsKey(userId), "验证码已失效");
        AssertUtil.assertTrue(V_POOL.get(userId).getValidateCode().equalsIgnoreCase(code), "验证码错误");
        P_POOL.put(userId, System.currentTimeMillis() / 1000);
        V_POOL.remove(userId);
    }

    /**
     * 验证状态判断
     *
     * @param userId
     * @return
     */
    public static boolean validate(String userId) {
        return P_POOL.containsKey(userId);
    }

    /**
     * 验证状态更新
     *
     * @param userId
     */
    public static void afterCaptcha(String userId) {
        P_POOL.remove(userId);
    }

    /**
     * 生成登陆验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initLoginSmsCode(String mobile) {
        initSmsCode(mobile, "进行登陆操作");
    }

    /**
     * 生成注册验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initRegisterSmsCode(String mobile) {
        initSmsCode(mobile, "注册新账号");
    }

    /**
     * 生成密码重置验证码（数字）并发送短信
     *
     * @param mobile
     */
    public void initResetSmsCode(String mobile) {
        initSmsCode(mobile, "重置密码");
    }

    private void initSmsCode(String mobile, String action) {
        AssertUtil.assertTrue(validateMobile(mobile), RestResultCode.ILLEGAL_PARAMETERS, "手机号格式非法");
        frequencyControl(mobile);

        if (C_POOL.containsKey(mobile)) {
            int current = C_POOL.get(mobile);
            AssertUtil.assertTrue(current <= MAX, RestResultCode.PARTIAL_CONTENT, "当日获取验证码已超限");
            C_POOL.replace(mobile, current, ++current);
        } else {
            C_POOL.put(mobile, 1);
        }

        List<String> variableList = new ArrayList<>(4);
        variableList.add(0, action);
        variableList.add(1, initCode(mobile, CaptchaType.NUMBER));
        variableList.add(2, String.valueOf(lifetime));

        AssertUtil.assertTrue(messageUtil.send(mobile, variableList).get("status").equals(0), "短信接口异常");
    }

    public boolean validateMobile(String mobile) {
        return mobile.matches(MOBILE_PHONE_REGEX);
    }

    /**
     * 验证码生命周期自动化管理。
     */
    @Scheduled(cron = CORN_STRING_CLEAN)
    private void cleanCycle() {
        AssertUtil.assertNotNull(lifetime, "生命周期设置异常");
        long currTime = System.currentTimeMillis() / 1000;
        for (Map.Entry<String, UserVerificationCode> i : V_POOL.entrySet()) {
            if (i.getValue().getTimestamp() + lifetime * 60 < currTime) {
                V_POOL.remove(i.getKey());
            }
        }
        for (Map.Entry<String, Long> i : P_POOL.entrySet()) {
            if (i.getValue() + PERIOD_OF_VALIDITY < currTime) {
                P_POOL.remove(i.getKey());
            }
        }
    }

    @Scheduled(cron = CORN_STRING_CONTROL)
    private void control() {
        for (Map.Entry<String, Integer> i : C_POOL.entrySet()) {
            C_POOL.remove(i.getKey());
        }
    }

}
