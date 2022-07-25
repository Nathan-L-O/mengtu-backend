package com.mengtu.kaichi.controller.label;

import com.mengtu.kaichi.label.pojo.Case;
import com.mengtu.kaichi.serviceimpl.label.service.CaseService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/case")
public class CaseController {

    @Resource
    private CaseService caseService;


    /*查询当前标签下的所有案例*/
    @GetMapping("/findAll")
    public Result<List<Case>> findAll(String labelId,Page<Case> page){
        return RestResultUtil.buildSuccessResult(caseService.selectAll(labelId),"查询成功");
    }

    @PostMapping("/add")
    public Result<String> add(Case c){
        int num = caseService.insert(c);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("添加成功");
        }
        return RestResultUtil.buildFailResult("添加失败");
    }

    @PostMapping("/update")
    public Result<String> update(Case c){
        int num = caseService.update(c);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("修改成功");
        }
        return RestResultUtil.buildFailResult("修改失败");
    }

    @PostMapping("/delete")
    public Result<String> delete(Case c){
        int num = caseService.delete(c.getCaseId());
        if (num == 1){
            return RestResultUtil.buildSuccessResult("删除成功");
        }
        return RestResultUtil.buildFailResult("删除失败");
    }

}
