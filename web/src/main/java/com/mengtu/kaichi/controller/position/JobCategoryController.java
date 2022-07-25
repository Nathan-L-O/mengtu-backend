package com.mengtu.kaichi.controller.position;

import com.mengtu.kaichi.position.pojo.JobCategory;
import com.mengtu.kaichi.position.pojo.Position;
import com.mengtu.kaichi.serviceimpl.position.service.JobCategoryService;
import com.mengtu.kaichi.serviceimpl.position.service.PositionService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/JobCategory")
public class JobCategoryController {

    @Resource
    private JobCategoryService jobCategoryService;

    @GetMapping("/selectAll")
    public Result<List<JobCategory>> selectAll(){
        return RestResultUtil.buildSuccessResult(jobCategoryService.selectAll(),"查询成功");
    }

    @PostMapping("/add")
    public Result<String> add(JobCategory jobCategory){
        int num = jobCategoryService.add(jobCategory);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("添加成功");
        }
        return RestResultUtil.buildFailResult("添加失败");
    }

    @PutMapping("/update")
    public Result<String> update(JobCategory jobCategory){
        int num = jobCategoryService.update(jobCategory);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("修改成功");
        }
        return RestResultUtil.buildFailResult("修改失败");
    }


    @DeleteMapping("/delete")
    public Result<String> delete(String jobCategoryId){
        int num = jobCategoryService.delete(jobCategoryId);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("删除成功");
        }
        return RestResultUtil.buildFailResult("删除失败");
    }
}
