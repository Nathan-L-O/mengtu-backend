package com.mengtu.kaichi.linkpoint.dal.service;

import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;

import java.util.List;

/**
 * linkpoint 项目仓储服务
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:41
 */
public interface LinkpointVaultService {

    /**
     * 创建
     *
     * @param projectVaultBO
     * @return
     */
    ProjectVaultBO create(ProjectVaultBO projectVaultBO);

    /**
     * 查询
     *
     * @param projectVaultBO
     * @return
     */
    ProjectVaultBO query(ProjectVaultBO projectVaultBO);

    /**
     * 全查询
     *
     * @param projectVaultBO
     * @return
     */
    List<ProjectVaultBO> queryAll(ProjectVaultBO projectVaultBO);

    /**
     * 删除
     *
     * @param projectVaultBO
     */
    void delete(ProjectVaultBO projectVaultBO);

    /**
     * 修改
     *
     * @param projectVaultBO
     * @return
     */
    ProjectVaultBO modify(ProjectVaultBO projectVaultBO);

}
