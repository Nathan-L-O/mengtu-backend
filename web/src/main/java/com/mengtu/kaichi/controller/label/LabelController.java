package com.mengtu.kaichi.controller.label;

import com.mengtu.kaichi.label.pojo.Label;
import com.mengtu.kaichi.serviceimpl.label.service.LabelService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@ResponseBody
@RestController
@RequestMapping(value = "/label", produces = {"application/json;charset=UTF-8"})
public class LabelController {

    @Resource
    private LabelService labelService;

    /*查询所有标签*/
    @GetMapping("/findAll")
    public Result<List<Label>> selectAll(){
        return RestResultUtil.buildSuccessResult(labelService.selectAll(),"查询成功");
    }

    /*添加标签*/
    @PostMapping("/add")
    public Result<String> add(@RequestParam("labelName") String labelName){
        int num = labelService.addByName(labelName);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("添加成功");
        }
        return RestResultUtil.buildFailResult("添加失败");
    }

    /*更新数据*/
    @PostMapping("/update")
    public Result<String> update(Label label){
        int num = labelService.updateById(label);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("更新成功");
        }
        return RestResultUtil.buildFailResult("更新失败");
    }

    /*删除标签,通过id*/
    @PostMapping("/delete")
    public Result<String> delete(Label label){
        int num = labelService.deleteById(label.getLabelId());
        if (num == 1){
            return RestResultUtil.buildSuccessResult("删除成功");
        }
        return RestResultUtil.buildFailResult("删除失败");
    }
}
