package com.mengtu.kaichi.serviceimpl.position;

import com.mengtu.kaichi.position.mapper.JobCategoryMapper;
import com.mengtu.kaichi.position.pojo.JobCategory;
import com.mengtu.kaichi.serviceimpl.position.service.JobCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class JobCategoryServiceImpl implements JobCategoryService {

    @Resource
    private JobCategoryMapper jobCategoryMapper;

    @Override
    public List<JobCategory> selectAll() {
        return jobCategoryMapper.selectList(null);
    }

    @Override
    public int add(JobCategory jobCategory) {
        return jobCategoryMapper.insert(jobCategory);
    }

    @Override
    public int update(JobCategory jobCategory) {
        return jobCategoryMapper.updateById(jobCategory);
    }

    @Override
    public int delete(String jobCategoryId) {
        return jobCategoryMapper.deleteById(jobCategoryId);
    }
}
