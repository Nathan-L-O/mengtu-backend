package com.mengtu.kaichi.user.dal.repo.perm;

import com.mengtu.kaichi.user.dal.model.perm.PermDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/25 09:52
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface PermDORepo extends JpaRepository<PermDO, Long> {

    /**
     * 通过权限id获取权限
     *
     * @param permIds
     * @return
     */
    List<PermDO> findAllByPermIdIn(List<String> permIds);

    /**
     * 查询权限
     *
     * @param permId
     * @return
     */
    PermDO findByPermId(String permId);

    /**
     * 查询权限
     *
     * @param permType
     * @return
     */
    PermDO findByPermType(String permType);

    /**
     * 查询权限
     *
     * @param permTypes
     * @return
     */
    List<PermDO> findAllByPermTypeIn(List<String> permTypes);

    /**
     * 通过拓展信息查询
     *
     * @param exiInfo
     * @return
     */
    PermDO findByExtInfo(String exiInfo);

    /**
     * 删除权限
     *
     * @param permId
     */
    void deleteByPermId(String permId);

}
