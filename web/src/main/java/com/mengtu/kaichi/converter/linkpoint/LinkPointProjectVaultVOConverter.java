package com.mengtu.kaichi.converter.linkpoint;

import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVaultVO;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.tools.AssertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Vault 模型转换器
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 10:19
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class LinkPointProjectVaultVOConverter {

    private static final String SEPARATOR = "#";


    public static ProjectVaultVO convert(ProjectVaultBO projectVaultBO) {
        if (projectVaultBO == null) {
            return null;
        }
        ProjectVaultVO projectVaultVO = new ProjectVaultVO();
        projectVaultVO.setModelId(projectVaultBO.getModelId());

        String originalName = projectVaultBO.getModelName();
        int flag = originalName.lastIndexOf("/");
        if (flag != -1) {
            projectVaultVO.setPath(originalName.substring(0, flag + 1));
            projectVaultVO.setModelName(originalName.substring(flag + 1));
        } else {
            projectVaultVO.setPath("/");
            projectVaultVO.setModelName(originalName);
        }
        projectVaultVO.setCreateDate(projectVaultBO.getCreateDate());
        projectVaultVO.setModifyDate(projectVaultBO.getModifyDate());
        projectVaultVO.setStatus(projectVaultBO.getStatus());
        if (projectVaultBO.getHashtag() != null) {
            projectVaultVO.setHashtagList(getList(projectVaultBO.getHashtag()));
        } else {
            projectVaultVO.setHashtagList(new ArrayList<>());
        }
        projectVaultVO.setExtInfo(projectVaultBO.getExtInfo());

        return projectVaultVO;
    }

    public static int getSize(String uri) {
        AssertUtil.assertStringNotBlank(uri, RestResultCode.ILLEGAL_PARAMETERS, "非法标签: 空");
        AssertUtil.assertTrue(!uri.endsWith(SEPARATOR), RestResultCode.ILLEGAL_PARAMETERS, "非法标签: 空参数");
        AssertUtil.assertTrue(uri.lastIndexOf(SEPARATOR + SEPARATOR) == -1, RestResultCode.ILLEGAL_PARAMETERS, "非法标签: 无效标识符");
        AssertUtil.assertTrue(uri.lastIndexOf("\\") == -1, RestResultCode.ILLEGAL_PARAMETERS, "非法标签: 禁止转义字符");

        int size = 0;
        while (uri.contains(SEPARATOR)) {
            size++;
            uri = uri.substring(uri.indexOf(SEPARATOR) + 1);
        }
        return size;
    }

    public static List<String> getList(String uri) {
        List<String> hashtagList = new ArrayList<>();
        int size = getSize(uri);
        for (int i = 0; i < size; i++) {
            hashtagList.add(uri.substring(uri.lastIndexOf(SEPARATOR) + 1));
            uri = uri.substring(0, uri.lastIndexOf(SEPARATOR));
        }
        return hashtagList;
    }


}
