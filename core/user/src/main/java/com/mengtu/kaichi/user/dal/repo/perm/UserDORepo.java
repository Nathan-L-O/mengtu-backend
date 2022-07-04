package com.mengtu.kaichi.user.dal.repo.perm;

import com.mengtu.kaichi.user.dal.model.perm.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户实体仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:34
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface UserDORepo extends JpaRepository<UserDO, Long> {

    /**
     * 通过用户名获取用户实体
     *
     * @param userName
     * @return
     */
    UserDO findByUsername(String userName);

    /**
     * 通过用户 ID 获取用户实体
     *
     * @param userId
     * @return
     */
    UserDO findByUserId(String userId);

    /**
     * 通过 sessionId 获取用户实体
     *
     * @param sessionId
     * @return
     */
    UserDO findBySessionId(String sessionId);

    /**
     * 批量获取用户
     *
     * @param userIds
     * @return
     */
    List<UserDO> findAllByUserIdIn(List<String> userIds);

    /**
     * 通过用户id 检查用户是存在
     *
     * @param userId
     * @return
     */
    boolean existsByUserId(String userId);
}
