package com.mengtu.kaichi.controller.linkpoint;

import com.mengtu.kaichi.common.annotation.Limit;
import com.mengtu.kaichi.common.annotation.ValidateToken;
import com.mengtu.kaichi.common.log.LoggerName;
import com.mengtu.kaichi.common.template.RestOperateCallBack;
import com.mengtu.kaichi.common.template.RestOperateTemplate;
import com.mengtu.kaichi.converter.linkpoint.LinkpointProjectVOConverter;
import com.mengtu.kaichi.model.linkpoint.request.ProjectRestRequest;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVO;
import com.mengtu.kaichi.position.pojo.LinkPointFolder;
import com.mengtu.kaichi.serviceimpl.linkpoint.builder.ProjectRequestBuilder;
import com.mengtu.kaichi.serviceimpl.position.service.LinkPointFolderService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.common.Result;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.log.Log;
import com.mengtu.util.tools.AssertUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin
@RestController
@ResponseBody
@RequestMapping(value = "/linkpoint/folder", produces = {"application/json;charset=UTF-8"})
public class LinkPointFolderController {
    /**
     * 日志实体
     */
    private final Logger LOGGER = LoggerFactory.getLogger(LinkPointFolderController.class);

    @Resource
    private LinkPointFolderService linkPointFolderService;

    /**
     * 创建 LinkPoint 文件夹
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<LinkPointFolder> createFolder(ProjectRestRequest request, HttpServletRequest httpServletRequest,LinkPointFolder linkPointFolder) {
        return RestOperateTemplate.operate(LOGGER, "创建 LinkPoint 文件夹", request, new RestOperateCallBack<LinkPointFolder>() {
            @Override
            public void before() {
              }

            @Override
            public Result<LinkPointFolder> execute() {
                linkPointFolderService.insert(linkPointFolder);
                return RestResultUtil.buildSuccessResult(
                        linkPointFolder , "创建成功");
            }
        });
    }

    /**
     * 重命名 LinkPoint 文件夹
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @PutMapping
    @Limit(threshold = 5)
    @Log(loggerName = LoggerName.WEB_DIGEST)
    public Result<LinkPointFolder> updateFolder(ProjectRestRequest request, HttpServletRequest httpServletRequest,LinkPointFolder linkPointFolder) {
        return RestOperateTemplate.operate(LOGGER, "创建 LinkPoint 文件夹", request, new RestOperateCallBack<LinkPointFolder>() {
            @Override
            public void before() {
              }

            @Override
            public Result<LinkPointFolder> execute() {
                linkPointFolderService.update(linkPointFolder);
                return RestResultUtil.buildSuccessResult(
                        linkPointFolder , "更新成功");
            }
        });
    }
}
