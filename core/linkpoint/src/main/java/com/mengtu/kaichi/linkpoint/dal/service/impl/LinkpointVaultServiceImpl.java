package com.mengtu.kaichi.linkpoint.dal.service.impl;

import com.mengtu.kaichi.linkpoint.dal.convert.EntityConverter;
import com.mengtu.kaichi.linkpoint.dal.model.LinkpointVaultDO;
import com.mengtu.kaichi.linkpoint.dal.repo.LinkpointVaultRepo;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointVaultService;
import com.mengtu.kaichi.linkpoint.idfactory.BizIdFactory;
import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.util.enums.RestResultCode;
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
 * linkpoint vault 仓储实现
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:41
 */
@Service
public class LinkpointVaultServiceImpl implements LinkpointVaultService {

    private static final String SORT_FLAG = "sort";

    @Resource
    BizIdFactory bizIdFactory;
    @Resource
    private LinkpointVaultRepo linkpointVaultRepo;

    @Override
    public ProjectVaultBO create(ProjectVaultBO projectVaultBO) {
        AssertUtil.assertStringNotBlank(projectVaultBO.getModelName(), "模型名称不能为空");

        LinkpointVaultDO linkpointVaultDO = linkpointVaultRepo.findByModelName(projectVaultBO.getModelName());
        AssertUtil.assertNull(linkpointVaultDO, "同名模型已存在");

        if (StringUtils.isBlank(projectVaultBO.getModelId())) {
            projectVaultBO.setModelId(bizIdFactory.getModelId());
        }

        if (StringUtils.isBlank(projectVaultBO.fetchExtInfo(SORT_FLAG))) {
            projectVaultBO.putExtInfo(SORT_FLAG, "0");
        }
        return EntityConverter.convert(linkpointVaultRepo.save(EntityConverter.convert(projectVaultBO)));
    }

    @Override
    public List<ProjectVaultBO> queryAll(ProjectVaultBO projectVaultBO) {
        AssertUtil.assertNotNull(projectVaultBO.getStatus(), "状态不能为空");

        List<LinkpointVaultDO> linkpointVaultDOList;
        if (projectVaultBO.getHashtag() == null) {
            linkpointVaultDOList = linkpointVaultRepo.findAllByStatus(projectVaultBO.getStatus());
        } else {
            linkpointVaultDOList = linkpointVaultRepo.findAllByStatusAndHashtagLike(projectVaultBO.getStatus(),
                    "%" + projectVaultBO.getHashtag() + "%");
        }
        return CollectionUtil.toStream(linkpointVaultDOList)
                .filter(Objects::nonNull)
                .map(EntityConverter::convert)
                .sorted(Comparator.comparingLong(o -> o.getCreateDate().getTime()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectVaultBO query(ProjectVaultBO projectVaultBO) {
        AssertUtil.assertNotNull(projectVaultBO.getModelId(), "模型 ID 不能为空");
        projectVaultBO = EntityConverter.convert(linkpointVaultRepo.findByModelId(projectVaultBO.getModelId()));
        AssertUtil.assertNotNull(projectVaultBO, RestResultCode.NOT_FOUND, "模型 ID 不存在");
        return projectVaultBO;
    }

    @Override
    public void delete(ProjectVaultBO projectVaultBO) {
        linkpointVaultRepo.deleteByModelId(projectVaultBO.getModelId());
    }

    @Override
    public ProjectVaultBO modify(ProjectVaultBO projectVaultBO) {
        LinkpointVaultDO newLinkpointVaultDO = EntityConverter.convert(projectVaultBO);
        LinkpointVaultDO linkpointVaultDO = linkpointVaultRepo.findByModelId(projectVaultBO.getModelId());
        if (newLinkpointVaultDO.getModelName() != null) {
            linkpointVaultDO.setModelName(newLinkpointVaultDO.getModelName());
        }
        if (newLinkpointVaultDO.getExtInfo() != null) {
            linkpointVaultDO.setExtInfo(newLinkpointVaultDO.getExtInfo());
        }
        if (newLinkpointVaultDO.getHashtag() != null) {
            linkpointVaultDO.setHashtag(newLinkpointVaultDO.getHashtag());
        }

        return EntityConverter.convert(linkpointVaultRepo.save(linkpointVaultDO));
    }
}
