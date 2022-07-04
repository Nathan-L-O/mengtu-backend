package com.mengtu.kaichi.linkshow.idfactory;

/**
 * 业务 id 工厂
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:10
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

}
