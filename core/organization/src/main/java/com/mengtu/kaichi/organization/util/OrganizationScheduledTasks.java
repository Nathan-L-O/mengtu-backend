package com.mengtu.kaichi.organization.util;

import com.mengtu.kaichi.organization.dal.convert.EntityConverter;
import com.mengtu.kaichi.organization.dal.repo.OrganizationInvitationRepo;
import com.mengtu.kaichi.organization.dal.service.OrganizationInvitationRepoService;
import com.mengtu.kaichi.organization.model.OrganizationInvitationBO;
import com.mengtu.util.tools.CollectionUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Organization 计划任务
 *
 * @author 过昊天
 * @version 1.0 @ 2022/6/6 10:28
 */
@Component
public class OrganizationScheduledTasks {
    private final Logger LOGGER = LoggerFactory.getLogger(OrganizationScheduledTasks.class);

    /**
     * CronString for clean cycle
     */
    private static final String CRON_STRING_CLEAN = "0 0/1 * * * ?";

    @Resource
    OrganizationInvitationRepo organizationInvitationRepo;

    @Resource
    OrganizationInvitationRepoService organizationInvitationRepoService;

    @Scheduled(cron = CRON_STRING_CLEAN)
    private void cleanCycle() {
        List<OrganizationInvitationBO> organizationInvitationBOList = CollectionUtil.toStream(organizationInvitationRepo.findAll())
                .filter(Objects::nonNull)
                .map(EntityConverter::convert)
                .collect(Collectors.toList());

        int count = 0;
        for (OrganizationInvitationBO organizationInvitationBO : organizationInvitationBOList) {
            if (new Date().after(organizationInvitationBO.getLifetime())) {
                organizationInvitationRepoService.delete(organizationInvitationBO.getOrganizationInvitationId());
                LoggerUtil.warn(LOGGER, "计划任务 - 组织邀请删除动作执行, 邀请 ID = {0}, 组织 ID = {1}", organizationInvitationBO.getOrganizationInvitationId(), organizationInvitationBO.getOrganizationId());
                count++;
            }
        }

        LoggerUtil.info(LOGGER, "计划任务 @ {0} - 找到 {1} 个组织邀请, {2} 个已过期邀请被删除", new Date(), organizationInvitationBOList.size(), count);
    }
}
