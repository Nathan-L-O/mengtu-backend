package com.mengtu.kaichi.linkshow.dal.service.impl;

import com.mengtu.kaichi.linkshow.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkshow.dal.repo.LinkshowProjectRepo;
import com.mengtu.kaichi.linkshow.dal.repo.LinkshowProjectVersionRepo;
import com.mengtu.kaichi.linkshow.dal.service.LinkshowProjectVersionRepoService;
import com.mengtu.kaichi.linkshow.idfactory.BizIdFactory;
import com.mengtu.kaichi.linkshow.model.ProjectVersionBO;
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
 * linkshow 项目版本仓储实现
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:40
 */
@Service
public class LinkshowProjectVersionRepoServiceImpl implements LinkshowProjectVersionRepoService {

    @Resource
    private LinkshowProjectRepo linkshowProjectRepo;

    @Resource
    private LinkshowProjectVersionRepo linkshowProjectVersionRepo;

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

        return EntityConverter.convert(linkshowProjectVersionRepo.save(EntityConverter.convert(projectVersionBO)));
    }

    @Override
    public List<ProjectVersionBO> queryByProjectId(String projectId) {
        return CollectionUtil.toStream(linkshowProjectVersionRepo.findAllByProjectId(projectId))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getGmtCreate().getTime()))
                .map(EntityConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjectVersion(String projectVersionId) {
        linkshowProjectVersionRepo.deleteByProjectVersionId(projectVersionId);
    }

    @Override
    public ProjectVersionBO queryByProjectVersionId(String projectVersionId) {
        return EntityConverter.convert(linkshowProjectVersionRepo.findByProjectVersionId(projectVersionId));
    }

}
