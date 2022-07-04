package com.mengtu.kaichi.linkshow.dal.repo;

import com.mengtu.kaichi.linkshow.dal.model.LinkshowProjectVersionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * linkshow 项目版本仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 10:19
 */
@Repository
public interface LinkshowProjectVersionRepo extends JpaRepository<LinkshowProjectVersionDO, Long> {

    /**
     * 查询项目 ID
     *
     * @param projectId
     * @return
     */
    List<LinkshowProjectVersionDO> findAllByProjectId(String projectId);

    /**
     * 查询项目版本 ID
     *
     * @param projectVersionId
     * @return
     */
    LinkshowProjectVersionDO findByProjectVersionId(String projectVersionId);

    /**
     * 删除项目版本
     *
     * @param projectId
     */
    void deleteAllByProjectId(String projectId);

    /**
     * 删除指定项目版本
     *
     * @param projectVersionId
     */
    void deleteByProjectVersionId(String projectVersionId);
}