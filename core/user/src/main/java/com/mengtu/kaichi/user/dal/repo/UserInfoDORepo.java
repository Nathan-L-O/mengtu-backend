package com.mengtu.kaichi.user.dal.repo;

import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户信息仓储
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/24 13:34
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Repository
public interface UserInfoDORepo extends JpaRepository<UserInfoDO, Long> {

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    UserInfoDO findByUserId(String userId);
}
