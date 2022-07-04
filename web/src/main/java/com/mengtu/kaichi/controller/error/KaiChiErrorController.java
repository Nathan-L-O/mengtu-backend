package com.mengtu.kaichi.controller.error;

import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * 错误控制器，接管 ErrorController 并以 rest 风格返回
 *
 * @author 过昊天 @ 2022/4/21 15:07
 * @version 1.0
 */
@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/error", produces = {"application/json;charset=UTF-8"})
public class KaiChiErrorController implements ErrorController {

    @Resource
    private ErrorAttributes errorAttributes;

    @RequestMapping
    public Result<RestResultCode> restErrorReturn(HttpServletRequest request, HttpServletResponse response) {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);

        Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(servletWebRequest, false);
        String errorCode = String.valueOf(errorAttributes.get("status"));
        String errorMsg = String.valueOf(errorAttributes.get("message"));
        response.setStatus(200);

        return RestResultUtil.buildResult(Objects.requireNonNull(RestResultCode.getByCode(errorCode)), errorMsg);
    }

    @Override
    public String getErrorPath() {
        return null;
    }

}
