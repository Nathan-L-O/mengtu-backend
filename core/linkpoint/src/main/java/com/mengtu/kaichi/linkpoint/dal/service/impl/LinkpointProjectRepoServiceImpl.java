package com.mengtu.kaichi.linkpoint.dal.service.impl;

import com.alibaba.fastjson.JSON;
import com.mengtu.kaichi.linkpoint.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkpoint.dal.model.LinkpointProjectDO;
import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointProjectRepo;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectRepoService;
import com.mengtu.kaichi.linkpoint.idfactory.BizIdFactory;
import com.mengtu.kaichi.linkpoint.model.ProjectBO;
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
 * linkpoint 项目仓储实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:40
 */
@Service
public class LinkpointProjectRepoServiceImpl implements LinkpointProjectRepoService {

    @Resource
    private LinkpointProjectRepo linkpointProjectRepo;

    @Resource
    BizIdFactory bizIdFactory;

    @Override
    public ProjectBO createProject(ProjectBO projectBO) {
        AssertUtil.assertStringNotBlank(projectBO.getProjectName(), "项目名称不能为空");

        LinkpointProjectDO organizationDO = linkpointProjectRepo.findByProjectNameAndDomainId(
                projectBO.getProjectName(), projectBO.getDomainId());
        AssertUtil.assertNull(organizationDO, "同名项目已存在");

        if (StringUtils.isBlank(projectBO.getProjectId())) {
            projectBO.setProjectId(bizIdFactory.getProjectId());
        }
        return EntityConverter.convert(linkpointProjectRepo.save(EntityConverter.convert(projectBO)));
    }

    @Override
    public ProjectBO updateProject(ProjectBO projectBO) {


        LinkpointProjectDO linkpointProjectDO = linkpointProjectRepo.findByProjectId(projectBO.getProjectId());
        AssertUtil.assertNotNull(linkpointProjectDO, "修改的项目信息不存在");

        if (projectBO.getProjectName() != null || projectBO.getProjectDescription() != null) {
            if (projectBO.getArchiveStatus() == null) {
                LinkpointProjectDO organizationDO = linkpointProjectRepo.findByProjectNameAndDomainId(
                        projectBO.getProjectName(), linkpointProjectDO.getDomainId());
                AssertUtil.assertNull(organizationDO, "同名项目已存在");

            }
        }

        LinkpointProjectDO newLinkpointProjectDO = EntityConverter.convert(projectBO);

        if (newLinkpointProjectDO.getProjectName() != null) {
            linkpointProjectDO.setProjectName(newLinkpointProjectDO.getProjectName());
        }
        if (newLinkpointProjectDO.getProjectDescription() != null) {
            linkpointProjectDO.setProjectDescription(newLinkpointProjectDO.getProjectDescription());
        }
        if (newLinkpointProjectDO.getArchiveStatus() != null) {
            linkpointProjectDO.setArchiveStatus(newLinkpointProjectDO.getArchiveStatus());
        }
        if (newLinkpointProjectDO.getStatus() != null) {
            linkpointProjectDO.setStatus(newLinkpointProjectDO.getStatus());
        }
        if (newLinkpointProjectDO.getDomainId() != null) {
            linkpointProjectDO.setDomainId(newLinkpointProjectDO.getDomainId());
        }
        if (projectBO.getExtInfo() == null) {
            linkpointProjectDO.setExtInfo(JSON.toJSONString(new HashMap<>(0)));
        } else if (!projectBO.getExtInfo().isEmpty()) {
            linkpointProjectDO.setExtInfo(newLinkpointProjectDO.getExtInfo());
        }

        return EntityConverter.convert(linkpointProjectRepo.save(linkpointProjectDO));
    }

    @Override
    public ProjectBO queryProject(ProjectBO projectBO) {
        LinkpointProjectDO linkpointProjectDO = linkpointProjectRepo.findByProjectId(projectBO.getProjectId());
        AssertUtil.assertNotNull(linkpointProjectDO, "项目 ID 不存在");
        return EntityConverter.convert(linkpointProjectDO);
    }

    @Override
    public List<ProjectBO> queryAllProject(ProjectBO projectBO) {
        return CollectionUtil.toStream(linkpointProjectRepo.findAllByDomainId(projectBO.getDomainId()))
                .filter(Objects::nonNull)
                .filter(o -> o.getFolderId() == null)
                .sorted(Comparator.comparingLong(o -> o.getGmtCreate().getTime()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectBO> queryByFolderId(String folderId) {
        return CollectionUtil.toStream(linkpointProjectRepo.findAllByFolderId(folderId))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getGmtCreate().getTime()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

}
