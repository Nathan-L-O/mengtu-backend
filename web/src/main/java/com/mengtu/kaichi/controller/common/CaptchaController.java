package com.mengtu.kaichi.controller.common;

import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.model.user.request.UserRequestBase;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.verification.VerificationCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Captcha 控制器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:57
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/common/captcha", produces = {"application/json;charset=UTF-8"})
public class CaptchaController {
    private final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class);

    @GetMapping
    public void getCaptcha(UserRequestBase request, HttpServletResponse response,
                           @RequestParam(value = "width", required = false, defaultValue = "200") String width,
                           @RequestParam(value = "height", required = false, defaultValue = "69") String height) {

        try {
            BufferedImage verifyImg = new BufferedImage(Integer.parseInt(width), Integer.parseInt(height), BufferedImage.TYPE_INT_RGB);
            VerificationCodeUtil.initCode(request.getUsername(), verifyImg, Integer.parseInt(width), Integer.parseInt(height));
            response.setContentType("image/png");
            OutputStream os = response.getOutputStream();
            ImageIO.write(verifyImg, "png", os);
            os.flush();
            os.close();
        } catch (IOException e) {
            throw new KaiChiException(e);
        }
    }

    @PostMapping
    public Result<Object> verifyCaptcha(UserRequestBase request, @RequestParam(required = false, value = "code") String code) {
        return RestOperateTemplate.operate(LOGGER, "图形验证码校验", null, new RestOperateCallBack<Object>() {
            @Override
            public void before() {
                AssertUtil.assertStringNotBlank(request.getUsername(), RestResultCode.ILLEGAL_PARAMETERS, "用户名不能为空");
                AssertUtil.assertStringNotBlank(code, RestResultCode.ILLEGAL_PARAMETERS, "验证码不能为空");
            }

            @Override
            public Result<Object> execute() {
                VerificationCodeUtil.validate(request.getUsername(), code);
                return RestResultUtil.buildSuccessResult("验证通过");
            }
        });
    }

}
