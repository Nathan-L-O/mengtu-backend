package com.mengtu.kaichi.linkpoint.util;

import com.mengtu.kaichi.linkpoint.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointProjectRepo;
import com.mengtu.kaichi.linkpoint.manager.LinkpointProjectManager;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
import com.mengtu.kaichi.linkpoint.request.ProjectManageRequest;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Linkpoint 计划任务
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/20 09:47
 */
@Component
public class LinkpointScheduledTasks {
    private final Logger LOGGER = LoggerFactory.getLogger(LinkpointScheduledTasks.class);

    /**
     * CronString for clean cycle
     */
    private static final String CRON_STRING_CLEAN = "0 0/1 * * * ?";

    @Resource
    LinkpointProjectRepo linkpointProjectRepo;
    @Resource
    LinkpointProjectManager linkpointProjectManager;

    @Scheduled(cron = CRON_STRING_CLEAN)
    private void cleanCycle() {
        List<ProjectBO> projectBOList = CollectionUtil.toStream(linkpointProjectRepo.findAllByArchiveStatus(1))
                .filter(Objects::nonNull)
                .map(EntityConverter::convert)
                .collect(Collectors.toList());

        Calendar calendar = new GregorianCalendar();

        int count = 0;

        for (ProjectBO projectBO : projectBOList) {
            calendar.setTime(projectBO.getModifyDate());
            calendar.add(Calendar.DATE, 15);

            if (new Date().after(calendar.getTime())) {
                ProjectManageRequest projectManageRequest = new ProjectManageRequest();
                projectManageRequest.setProjectId(projectBO.getProjectId());
                linkpointProjectManager.deleteAllVersion(projectManageRequest);
                LoggerUtil.warn(LOGGER, "计划任务 - LinkPoint 删除动作执行, 项目名称 = {0}, 项目 ID = {1}", projectBO.getProjectName(), projectBO.getProjectId());
                count++;
            }
        }

        LoggerUtil.info(LOGGER, "计划任务 @ {0} - 找到 {1} 个已归档的 LinkPoint项目, {2} 个已过期项目被删除", new Date(), projectBOList.size(), count);
    }
}
