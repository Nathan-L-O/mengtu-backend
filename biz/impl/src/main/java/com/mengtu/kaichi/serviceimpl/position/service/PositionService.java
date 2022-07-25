package com.mengtu.kaichi.serviceimpl.position.service;


import com.mengtu.kaichi.position.pojo.Position;

import java.util.List;

public interface PositionService {

    /*根据类型id查找所有*/
    List<Position> selectAll(String jobCategoryId);

    /*添加工作*/
    int add(Position position);

    /*修改工作信息*/
    int update(Position position);

    /*删除工作*/
    int delete(String positionId);
}
