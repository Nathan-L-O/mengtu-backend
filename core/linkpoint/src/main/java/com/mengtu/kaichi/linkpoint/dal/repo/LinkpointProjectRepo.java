package com.mengtu.kaichi.linkpoint.dal.repo;

import com.mengtu.kaichi.linkpoint.dal.model.LinkpointProjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * linkpoint 项目仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:19
 */
@Repository
public interface LinkpointProjectRepo extends JpaRepository<LinkpointProjectDO, Long> {

    /**
     * 查询项目 ID
     *
     * @param projectId
     * @return
     */
    LinkpointProjectDO findByProjectId(String projectId);

    List<LinkpointProjectDO> findAllByFolderId (String folderId);

    List<LinkpointProjectDO> findAllByFolderIdAndIsPrincipalProject (String folderId,Boolean isPrincipalProject);

    /**
     * 查询项目名称与域 ID
     *
     * @param projectName
     * @param domainId
     * @return
     */
    LinkpointProjectDO findByProjectNameAndDomainId(String projectName, String domainId);

    /**
     * 查询域 ID
     *
     * @param domainId
     * @return
     */
    List<LinkpointProjectDO> findAllByDomainId(String domainId);

    /**
     * 删除项目
     *
     * @param projectId
     */
    void deleteByProjectId(String projectId);

    /**
     * 通过归档状态查找
     *
     * @param status
     * @return
     */
    List<LinkpointProjectDO> findAllByArchiveStatus(Integer status);
}