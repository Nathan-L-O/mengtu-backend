package com.mengtu.kaichi.linkshow.dal.service.impl;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.linkshow.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkshow.dal.model.LinkshowProjectDO;
import com.mengtu.kaichi.linkshow.dal.repo.LinkshowProjectRepo;
import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectRepoService;
import com.mengtu.kaichi.linkshow.idfactory.BizIdFactory;
import com.mengtu.kaichi.linkshow.model.ProjectBO;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * linkshow 项目仓储实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:40
 */
@Service
public class LinkshowProjectRepoServiceImpl implements LinkshowProjectRepoService {

    @Resource
    private LinkshowProjectRepo linkshowProjectRepo;

    @Resource
    BizIdFactory bizIdFactory;

    @Override
    public ProjectBO createProject(ProjectBO projectBO) {
        AssertUtil.assertStringNotBlank(projectBO.getProjectName(), "项目名称不能为空");

        LinkshowProjectDO organizationDO = linkshowProjectRepo.findByProjectNameAndDomainId(
                projectBO.getProjectName(), projectBO.getDomainId());
        AssertUtil.assertNull(organizationDO, "同名项目已存在");

        if (StringUtils.isBlank(projectBO.getProjectId())) {
            projectBO.setProjectId(bizIdFactory.getProjectId());
        }
        return EntityConverter.convert(linkshowProjectRepo.save(EntityConverter.convert(projectBO)));
    }

    @Override
    public ProjectBO updateProject(ProjectBO projectBO) {


        LinkshowProjectDO linkshowProjectDO = linkshowProjectRepo.findByProjectId(projectBO.getProjectId());
        AssertUtil.assertNotNull(linkshowProjectDO, "修改的项目信息不存在");

        if (projectBO.getProjectName() != null || projectBO.getProjectDescription() != null) {
            if (projectBO.getArchiveStatus() == null) {
                LinkshowProjectDO organizationDO = linkshowProjectRepo.findByProjectNameAndDomainId(
                        projectBO.getProjectName(), linkshowProjectDO.getDomainId());
                AssertUtil.assertNull(organizationDO, "同名项目已存在");

            }
        }

        LinkshowProjectDO newLinkshowProjectDO = EntityConverter.convert(projectBO);

        if (newLinkshowProjectDO.getProjectName() != null) {
            linkshowProjectDO.setProjectName(newLinkshowProjectDO.getProjectName());
        }
        if (newLinkshowProjectDO.getProjectDescription() != null) {
            linkshowProjectDO.setProjectDescription(newLinkshowProjectDO.getProjectDescription());
        }
        if (newLinkshowProjectDO.getArchiveStatus() != null) {
            linkshowProjectDO.setArchiveStatus(newLinkshowProjectDO.getArchiveStatus());
        }
        if (newLinkshowProjectDO.getStatus() != null) {
            linkshowProjectDO.setStatus(newLinkshowProjectDO.getStatus());
        }
        if (newLinkshowProjectDO.getDomainId() != null) {
            linkshowProjectDO.setDomainId(newLinkshowProjectDO.getDomainId());
        }
        if (projectBO.getExtInfo() == null) {
            linkshowProjectDO.setExtInfo(JSON.toJSONString(new HashMap<>(0)));
        } else if (!projectBO.getExtInfo().isEmpty()) {
            linkshowProjectDO.setExtInfo(newLinkshowProjectDO.getExtInfo());
        }

        return EntityConverter.convert(linkshowProjectRepo.save(linkshowProjectDO));
    }

    @Override
    public ProjectBO queryProject(ProjectBO projectBO) {
        LinkshowProjectDO linkshowProjectDO = linkshowProjectRepo.findByProjectId(projectBO.getProjectId());
        AssertUtil.assertNotNull(linkshowProjectDO, "项目 ID 不存在");
        return EntityConverter.convert(linkshowProjectDO);
    }

    @Override
    public List<ProjectBO> queryAllProject(ProjectBO projectBO) {
        return CollectionUtil.toStream(linkshowProjectRepo.findAllByDomainId(projectBO.getDomainId()))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getGmtCreate().getTime()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

}
