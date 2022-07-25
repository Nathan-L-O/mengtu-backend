package com.mengtu.kaichi.serviceimpl.label.service;

import com.mengtu.kaichi.label.pojo.Label;

import java.util.List;

public interface LabelService {

    /*查询所有标签*/
    List<Label> selectAll();

    /*通过id删除标签*/
    int deleteById(String labelId);

    /*通过name删除标签*/
    int deleteByName(String labelName);

    /*通过id修改标签*/
    int updateById(Label label);

//    /*通过name修改标签*/
//    int updateByName(Label label);


    /*添加标签*/
    int addByName(String labelName);
}
