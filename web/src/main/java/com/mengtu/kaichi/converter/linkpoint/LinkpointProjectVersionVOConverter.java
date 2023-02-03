package com.mengtu.kaichi.converter.linkpoint;

import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVersionVO;
import com.mengtu.util.hash.Base64Util;

/**
 * 项目版本模型转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:36
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class LinkpointProjectVersionVOConverter {

    public static ProjectVersionVO convert(ProjectVersionBO projectVersionBO) {
        if (projectVersionBO == null) {
            return null;
        }
        ProjectVersionVO projectVersionVO = new ProjectVersionVO();
        projectVersionVO.setProjectId(projectVersionBO.getProjectId());
        projectVersionVO.setProjectVersionId(projectVersionBO.getProjectVersionId());
        projectVersionVO.setResourceUri(Base64Util.encodeData(projectVersionBO.getResourceUri()));
        projectVersionVO.setDomainId(projectVersionBO.getDomainId());
        projectVersionVO.setVersionTimestamp(projectVersionBO.getVersionTimestamp());
        projectVersionVO.setSignatureUrl("无");
        projectVersionVO.setDate(projectVersionBO.getDate());
        projectVersionVO.putExtInfo("operatorId", projectVersionBO.fetchExtInfo("operator_id"));
        projectVersionVO.putExtInfo("operatorName", projectVersionBO.fetchExtInfo("operator_name"));
        return projectVersionVO;
    }

}
