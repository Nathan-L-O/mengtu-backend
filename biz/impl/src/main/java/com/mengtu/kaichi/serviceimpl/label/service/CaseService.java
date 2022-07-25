package com.mengtu.kaichi.serviceimpl.label.service;

import com.mengtu.kaichi.label.pojo.Case;

import java.util.List;


public interface CaseService {

    /*查询当前标签下得所有案例*/
    List<Case> selectAll(String labelId);

    /*添加案例*/
    int insert(Case c);

    /*id修改案例*/
    int update(Case c);

    /*id删除案例*/
    int delete(String caseId);
}
