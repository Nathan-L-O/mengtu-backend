package com.mengtu.kaichi.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mengtu.kaichi.user.dal.model.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
public interface UserMapper extends BaseMapper<UserInfoDO> {
}
