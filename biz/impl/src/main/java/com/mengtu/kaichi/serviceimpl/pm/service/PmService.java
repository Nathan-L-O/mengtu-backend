package com.mengtu.kaichi.serviceimpl.pm.service;


import com.mengtu.kaichi.pm.pojo.Pm;

import java.util.List;

public interface PmService {

    /*根据类型id查找所有*/
    List<Pm> selectAll(Pm pm);

    /*添加工作*/
    int add(Pm pm);

    /*修改工作信息*/
    int update(Pm pm);

    /*删除工作*/
    int delete(Pm pm);

    /*查询*/
    Pm selectById(Pm pm);

    /*复制*/
    Pm duplicate(Pm pm);
}
