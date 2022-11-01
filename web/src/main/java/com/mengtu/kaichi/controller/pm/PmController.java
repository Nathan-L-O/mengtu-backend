package com.mengtu.kaichi.controller.pm;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkshow.LinkshowProjectVOConverter;
import com.mengtu.kaichi.model.linkshow.request.ProjectRestRequest;
import com.mengtu.kaichi.model.linkshow.vo.ProjectVO;
import com.mengtu.kaichi.pm.pojo.Pm;
import com.mengtu.kaichi.serviceimpl.linkshow.builder.ProjectRequestBuilder;
import com.mengtu.kaichi.serviceimpl.linkshow.service.ProjectService;
import com.mengtu.kaichi.serviceimpl.linkshow.service.ProjectVersionService;
import com.mengtu.kaichi.serviceimpl.pm.service.PmService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@CrossOrigin
@Controller(value = "pmController")
@ResponseBody
@RequestMapping(value = "/pm", produces = {"application/json;charset=UTF-8"})
public class PmController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(PmController.class);

    @Resource
    private PmService pmService;

    /**
     * 创建项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @ValidateToken
//    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Pm> createProject(ProjectRestRequest request, HttpServletRequest httpServletRequest, Pm pm) {
        return RestOperateTemplate.operate(LOGGER, "创建项目", request, new RestOperateCallBack<Pm>() {
            @Override
            public void before() {
            }

            @Override
            public Result<Pm> execute() {
                pm.setInitialId(request.getUserId());
                pmService.add(pm);
                return RestResultUtil.buildSuccessResult(
                        pm, "创建项目成功");
            }
        });
    }

    /**
     * 更新 linkshow 项目信息
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping
    @ValidateToken
//    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Pm> updateProject(ProjectRestRequest request, HttpServletRequest httpServletRequest,Pm pm) {
        return RestOperateTemplate.operate(LOGGER, "更新项目信息", request, new RestOperateCallBack<Pm>() {
            @Override
            public void before() {
            }

            @Override
            public Result<Pm> execute() {
                pmService.update(pm);
                return RestResultUtil.buildSuccessResult(
                        pm, "更新项目信息成功");
            }
        });
    }

    /**
     * 获取项目实体信息
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping
    @ValidateToken
//    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Pm> getProjectInstance(ProjectRestRequest request, HttpServletRequest httpServletRequest,Pm pm) {
        return RestOperateTemplate.operate(LOGGER, "查询项目实体", request, new RestOperateCallBack<Pm>() {
            @Override
            public void before() {
            }

            @Override
            public Result<Pm> execute() {
                Pm pms = pmService.selectById(pm);
                return RestResultUtil.buildSuccessResult(
                        pms, "获取项目实体信息成功");
            }
        });
    }

    /**
     * 获取项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @GetMapping(value = "/list")
    @ValidateToken
//    @Limit
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<List<Pm>> getProject(ProjectRestRequest request, HttpServletRequest httpServletRequest) {
        return RestOperateTemplate.operate(LOGGER, "查询项目", request, new RestOperateCallBack<List<Pm>>() {
            @Override
            public void before() {
                }

            @Override
            public Result<List<Pm>> execute() {
                Pm pm = new Pm();
                pm.setInitialId(request.getUserId());

                List<Pm> pms = pmService.selectAll(pm);
                return RestResultUtil.buildSuccessResult(pms, "获取项目列表成功");
            }
        });
    }

    /**
     * 复制 linkshow 项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping(value = "/duplicate")
    @ValidateToken
//    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Pm> duplicateProject(ProjectRestRequest request, HttpServletRequest httpServletRequest,Pm pm) {
        return RestOperateTemplate.operate(LOGGER, "复制项目", request, new RestOperateCallBack<Pm>() {
            @Override
            public void before() {
            }

            @Override
            public Result<Pm> execute() {

                return RestResultUtil.buildSuccessResult(
                        pmService.duplicate(pm), "复制项目成功");
            }
        });
    }

    /**
     * 删除项目
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @DeleteMapping
    @ValidateToken
//    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<Pm> deleteProject(ProjectRestRequest request, HttpServletRequest httpServletRequest,Pm pm) {
        return RestOperateTemplate.operate(LOGGER, "删除项目", request, new RestOperateCallBack<Pm>() {
            @Override
            public void before() {
            }

            @Override
            public Result<Pm> execute() {
                pmService.delete(pm);
                return RestResultUtil.buildSuccessResult("删除项目成功");
            }
        });
    }

}