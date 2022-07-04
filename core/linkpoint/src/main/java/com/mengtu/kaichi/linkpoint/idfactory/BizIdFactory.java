package com.mengtu.kaichi.linkpoint.idfactory;

/**
 * 业务 id 工厂
 *
 * @author 过昊天
 * @version 1.1 @ 2022/5/31 11:22
 */
public interface BizIdFactory {

    /**
     * 生成项目 ID
     *
     * @return
     */
    String getProjectId();

    /**
     * 生成项目版本 ID
     *
     * @param projectId
     * @return
     */
    String getProjectVersionId(String projectId);

    /**
     * 生成项目版本 ID
     *
     * @return
     */
    String getModelId();

}
