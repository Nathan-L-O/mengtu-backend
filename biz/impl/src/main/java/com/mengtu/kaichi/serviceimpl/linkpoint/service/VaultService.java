package com.mengtu.kaichi.serviceimpl.linkpoint.service;

import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.kaichi.serviceimpl.linkpoint.request.VaultRequest;

import java.io.File;
import java.util.List;

/**
 * Vault 管理服务
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 10:59
 */
public interface VaultService {

    /**
     * 创建
     *
     * @param request
     * @param preview
     * @param file
     * @return
     */
    ProjectVaultBO create(VaultRequest request, File preview, File file);

    /**
     * 修改
     *
     * @param request
     * @param preview
     * @param file
     * @return
     */
    ProjectVaultBO modify(VaultRequest request, File preview, File file);

    /**
     * 创建
     *
     * @param request
     * @return
     */
    ProjectVaultBO get(VaultRequest request);

    /**
     * 查询
     *
     * @param request
     * @return
     */
    List<ProjectVaultBO> query(VaultRequest request);

    /**
     * 删除
     *
     * @param request
     */
    void delete(VaultRequest request);

    /**
     * 获取 TAG 列表
     *
     * @param request
     * @return
     */
    List<String> getTagList(VaultRequest request);
}