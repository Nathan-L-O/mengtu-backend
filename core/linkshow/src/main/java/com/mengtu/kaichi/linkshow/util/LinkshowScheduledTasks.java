package com.mengtu.kaichi.linkshow.util;

import com.mengtu.kaichi.linkshow.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkshow.dal.repo.LinkshowProjectRepo;
import com.mengtu.kaichi.linkshow.manager.LinkshowProjectManager;
import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.kaichi.linkshow.request.ProjectManageRequest;
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
 * Linkshow 计划任务
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/20 09:48
 */
@Component
public class LinkshowScheduledTasks {
    private final Logger LOGGER = LoggerFactory.getLogger(LinkshowScheduledTasks.class);

    /**
     * CronString for clean cycle
     */
    private static final String CRON_STRING_CLEAN = "0 0/1 * * * ?";

    @Resource
    LinkshowProjectRepo linkshowProjectRepo;
    @Resource
    LinkshowProjectManager linkshowProjectManager;

    @Scheduled(cron = CRON_STRING_CLEAN)
    private void cleanCycle() {
        List<ProjectBO> projectBOList = CollectionUtil.toStream(linkshowProjectRepo.findAllByArchiveStatus(1))
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
                linkshowProjectManager.deleteAllVersion(projectManageRequest);
                LoggerUtil.warn(LOGGER, "计划任务 - LinkShow 删除动作执行, 项目名称 = {0}, 项目 ID = {1}", projectBO.getProjectName(), projectBO.getProjectId());
                count++;
            }
        }

        LoggerUtil.info(LOGGER, "计划任务 @ {0} - 找到 {1} 个已归档的 LinkShow项目, {2} 个已过期项目被删除", new Date(), projectBOList.size(), count);
    }
}
