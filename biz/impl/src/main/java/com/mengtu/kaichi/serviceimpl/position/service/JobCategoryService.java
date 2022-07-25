package com.mengtu.kaichi.serviceimpl.position.service;


import com.mengtu.kaichi.position.pojo.JobCategory;

import java.util.List;

public interface JobCategoryService {

    /*查找所有*/
    List<JobCategory> selectAll();

    /*添加工作类别*/
    int add(JobCategory jobCategory);

    /*修改工作类别*/
    int update(JobCategory jobCategory);

    /*删除工作类别*/
    int delete(String jobCategoryId);
}
