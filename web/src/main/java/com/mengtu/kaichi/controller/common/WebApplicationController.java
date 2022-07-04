package com.mengtu.kaichi.controller.common;

import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.tools.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * WebApplicationController
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/29 14:31
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/common/app", produces = {"application/json;charset=UTF-8"})
public class WebApplicationController {
    @Resource
    private ApplicationContext applicationContext;

    @Value("${kaichi.webapp.admin_token}")
    private String adminToken;

    @DeleteMapping
    public void appShutdown(HttpServletRequest httpServletRequest) {
        String authRequest = httpServletRequest.getHeader("Authorization");
        AssertUtil.assertNotNull(authRequest, RestResultCode.ILLEGAL_PARAMETERS);
        AssertUtil.assertEquals(authRequest, adminToken, RestResultCode.FORBIDDEN);
        SpringApplication.exit(applicationContext);
    }

}
