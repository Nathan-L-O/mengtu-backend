package com.mengtu.kaichi.linkpoint.dal.repo;

import com.mengtu.kaichi.linkpoint.dal.model.LinkpointVaultDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * linkpoint vault 仓储
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:41
 */
@Repository
public interface LinkpointVaultRepo extends JpaRepository<LinkpointVaultDO, Long> {

    /**
     * 查询模型 ID
     *
     * @param modelId
     * @return
     */
    LinkpointVaultDO findByModelId(String modelId);

    /**
     * 查询模型名称
     *
     * @param modelName
     * @return
     */
    LinkpointVaultDO findByModelName(String modelName);

    /**
     * 通过归档状态查找
     *
     * @param status
     * @return
     */
    List<LinkpointVaultDO> findAllByStatus(Integer status);

    /**
     * 通过归档状态查找
     *
     * @param modelName
     * @return
     */
    List<LinkpointVaultDO> findAllByModelNameLike(String modelName);

    /**
     * 删除
     *
     * @param modelId
     */
    void deleteByModelId(String modelId);

    /**
     * 通过标签模糊查找
     *
     * @param status
     * @param key
     * @return
     */
    List<LinkpointVaultDO> findAllByStatusAndHashtagLike(Integer status, String key);

}