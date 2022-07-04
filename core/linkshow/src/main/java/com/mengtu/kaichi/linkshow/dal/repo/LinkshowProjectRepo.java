package com.mengtu.kaichi.linkshow.dal.repo;

import com.mengtu.kaichi.linkshow.dal.model.LinkshowProjectDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * linkshow 项目仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/20 09:30
 */
@Repository
public interface LinkshowProjectRepo extends JpaRepository<LinkshowProjectDO, Long> {

    /**
     * 查询项目 ID
     *
     * @param projectId
     * @return
     */
    LinkshowProjectDO findByProjectId(String projectId);

    /**
     * 查询项目名称与域 ID
     *
     * @param projectName
     * @param domainId
     * @return
     */
    LinkshowProjectDO findByProjectNameAndDomainId(String projectName, String domainId);

    /**
     * 查询域 ID
     *
     * @param domainId
     * @return
     */
    List<LinkshowProjectDO> findAllByDomainId(String domainId);

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
    List<LinkshowProjectDO> findAllByArchiveStatus(Integer status);
}