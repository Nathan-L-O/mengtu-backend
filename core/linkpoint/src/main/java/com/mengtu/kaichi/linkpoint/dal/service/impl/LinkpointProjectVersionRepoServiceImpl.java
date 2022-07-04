package com.mengtu.kaichi.linkpoint.dal.service.impl;

import com.mengtu.kaichi.linkpoint.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointProjectRepo;
import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointProjectVersionRepo;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectVersionRepoService;
import com.mengtu.kaichi.linkpoint.idfactory.BizIdFactory;
import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * linkpoint 项目版本仓储实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:40
 */
@Service
public class LinkpointProjectVersionRepoServiceImpl implements LinkpointProjectVersionRepoService {

    @Resource
    private LinkpointProjectRepo linkpointProjectRepo;

    @Resource
    private LinkpointProjectVersionRepo linkpointProjectVersionRepo;

    @Resource
    BizIdFactory bizIdFactory;

    @Override
    public ProjectVersionBO createProjectVersion(ProjectVersionBO projectVersionBO) {
        AssertUtil.assertStringNotBlank(projectVersionBO.getProjectId(), "项目 ID 不能为空");
        AssertUtil.assertStringNotBlank(projectVersionBO.getDomainId(), "域 ID 不能为空");

        if (StringUtils.isBlank(projectVersionBO.getProjectVersionId())) {
            projectVersionBO.setProjectVersionId(bizIdFactory.getProjectVersionId(projectVersionBO.getProjectId()));
        }
        if (StringUtils.isBlank(projectVersionBO.getResourceUri())) {
            projectVersionBO.setResourceUri(HashUtil.sha256(projectVersionBO.getProjectVersionId()));
        }

        return EntityConverter.convert(linkpointProjectVersionRepo.save(EntityConverter.convert(projectVersionBO)));
    }

    @Override
    public List<ProjectVersionBO> queryByProjectId(String projectId) {
        return CollectionUtil.toStream(linkpointProjectVersionRepo.findAllByProjectId(projectId))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getGmtCreate().getTime()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjectVersion(String projectVersionId) {
        linkpointProjectVersionRepo.deleteByProjectVersionId(projectVersionId);
    }

    @Override
    public ProjectVersionBO queryByProjectVersionId(String projectVersionId) {
        return EntityConverter.convert(linkpointProjectVersionRepo.findByProjectVersionId(projectVersionId));
    }

}
