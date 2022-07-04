package com.mengtu.kaichi.serviceimpl.linkpoint.service.impl;

import com.mengtu.kaichi.linkpoint.dal.service.LinkpointVaultService;
import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.kaichi.serviceimpl.linkpoint.request.VaultRequest;
import com.mengtu.kaichi.serviceimpl.linkpoint.service.VaultService;
import com.mengtu.util.enums.CommonResultCode;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import com.mengtu.util.hash.HashUtil;
import com.mengtu.util.storage.ObsUtil;
import com.mengtu.util.tools.AssertUtil;
import com.mengtu.util.tools.CollectionUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Vault 管理服务实现
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 11:15
 */
@Service
public class VaultServiceImpl implements VaultService {

    private static final String VAULT_LOCATION_MODEL = "vault/linkpoint/model/";
    private static final String VAULT_LOCATION_PREVIEW = "vault/linkpoint/preview/";

    private static final String SORT_FLAG = "sort";

    private static final String SEPARATOR = "#";

    @Resource
    private LinkpointVaultService linkpointVaultService;

    @Resource
    ObsUtil obsUtil;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectVaultBO create(VaultRequest request, File preview, File file) {
        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setModelName(request.getModelName());
        if (request.getStatus() == null) {
            projectVaultBO.setStatus(0);
        }
        if (request.getSort() != null) {
            projectVaultBO.putExtInfo(SORT_FLAG, request.getSort().toString());
        }
        if (request.getHashtag() != null) {
            projectVaultBO.setHashtag(request.getHashtag());
        }
        projectVaultBO = linkpointVaultService.create(projectVaultBO);
        AssertUtil.assertNotNull(projectVaultBO, CommonResultCode.SYSTEM_ERROR);

        String hash = HashUtil.sha256(projectVaultBO.getModelId());
        try {
            obsUtil.deleteFile(VAULT_LOCATION_MODEL, hash);
            obsUtil.deleteFile(VAULT_LOCATION_PREVIEW, hash);
        } catch (Exception ignored) {
        }
        String originalFileName = file.getName();
        obsUtil.uploadFile(file, VAULT_LOCATION_MODEL, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));
        originalFileName = preview.getName();
        obsUtil.uploadFile(preview, VAULT_LOCATION_PREVIEW, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));

        return projectVaultBO;
    }

    @Override
    public List<ProjectVaultBO> query(VaultRequest request) {
        List<ProjectVaultBO> projectVaultBOList = generalQuery(request);

        for (ProjectVaultBO temp : projectVaultBOList) {
            temp.putExtInfo("previewPictureUrl", obsUtil.getSignatureDownloadUrl(VAULT_LOCATION_PREVIEW,
                    HashUtil.sha256(temp.getModelId()), 14400L));
        }
        return projectVaultBOList;
    }

    @Override
    public ProjectVaultBO get(VaultRequest request) {
        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setModelId(request.getModelId());
        projectVaultBO = linkpointVaultService.query(projectVaultBO);

        try {
            projectVaultBO.putExtInfo("dataUrl", obsUtil.getSignatureDownloadUrl(VAULT_LOCATION_PREVIEW,
                    HashUtil.sha256(projectVaultBO.getModelId()), 120L));
        } catch (Exception ignored) {
        }

        return projectVaultBO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(VaultRequest request) {
        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setModelId(request.getModelId());
        linkpointVaultService.delete(projectVaultBO);
    }

    @Override
    public ProjectVaultBO modify(VaultRequest request, File preview, File file) {
        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setModelId(request.getModelId());
        projectVaultBO = linkpointVaultService.query(projectVaultBO);
        AssertUtil.assertNotNull(projectVaultBO, RestResultCode.NOT_FOUND, "模型 ID 不存在");

        if (request.getSort() != null) {
            projectVaultBO.putExtInfo(SORT_FLAG, request.getSort().toString());
        }
        if (request.getModelName() != null) {
            projectVaultBO.setModelName(request.getModelName());
        }
        if (request.getHashtag() != null) {
            projectVaultBO.setHashtag(request.getHashtag());
        }
        if (preview != null) {
            String hash = HashUtil.sha256(projectVaultBO.getModelId());
            String originalFileName = preview.getName();
            try {
                obsUtil.deleteFile(VAULT_LOCATION_PREVIEW, hash);
                obsUtil.uploadFile(file, VAULT_LOCATION_PREVIEW, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));
            } catch (Exception e) {
                throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "OBS 错误: 上传失败");
            }
        }
        if (file != null) {
            String hash = HashUtil.sha256(projectVaultBO.getModelId());
            String originalFileName = file.getName();
            try {
                obsUtil.deleteFile(VAULT_LOCATION_MODEL, hash);
                obsUtil.uploadFile(file, VAULT_LOCATION_MODEL, hash + originalFileName.substring(originalFileName.lastIndexOf(".")));
            } catch (Exception e) {
                throw new KaiChiException(CommonResultCode.SYSTEM_ERROR, "OBS 错误: 上传失败");
            }
        }
        return linkpointVaultService.modify(projectVaultBO);
    }

    @Override
    public List<String> getTagList(VaultRequest request) {
        List<String> hashtagList = new ArrayList<>();
        List<ProjectVaultBO> projectVaultBOList = generalQuery(request);

        for (ProjectVaultBO projectVaultBO : projectVaultBOList) {
            String hashtag = projectVaultBO.getHashtag();
            if (hashtag != null) {
                try {
                    AssertUtil.assertStringNotBlank(hashtag);
                    AssertUtil.assertTrue(!hashtag.endsWith(SEPARATOR));
                    AssertUtil.assertTrue(hashtag.lastIndexOf(SEPARATOR + SEPARATOR) == -1);
                    AssertUtil.assertTrue(hashtag.lastIndexOf("\\") == -1);
                    int size = 0;
                    while (hashtag.contains(SEPARATOR)) {
                        size++;
                        hashtag = hashtag.substring(hashtag.indexOf(SEPARATOR) + 1);
                    }
                    hashtag = projectVaultBO.getHashtag();
                    for (int i = 0; i < size; i++) {
                        String pending = hashtag.substring(hashtag.lastIndexOf(SEPARATOR) + 1);
                        if (!hashtagList.contains(pending)) {
                            hashtagList.add(pending);
                        }
                        hashtag = hashtag.substring(0, hashtag.lastIndexOf(SEPARATOR));
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return hashtagList;
    }

    private List<ProjectVaultBO> generalQuery(VaultRequest request) {
        ProjectVaultBO projectVaultBO = new ProjectVaultBO();
        projectVaultBO.setStatus(request.getStatus());
        projectVaultBO.setHashtag(request.getHashtag());
        return CollectionUtil.toStream(linkpointVaultService.queryAll(projectVaultBO))
                .filter(Objects::nonNull)
                .sorted(Comparator.comparingLong(o -> o.getCreateDate().getTime()))
                .collect(Collectors.toList());
    }
}
