package com.mengtu.kaichi.controller.position;

import com.mengtu.kaichi.position.pojo.Position;
import com.mengtu.kaichi.serviceimpl.position.service.PositionService;
import com.mengtu.kaichi.util.RestResultUtil;
import com.mengtu.util.common.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@CrossOrigin
@RestController
@ResponseBody
@RequestMapping(value = "/position", produces = {"application/json;charset=UTF-8"})
public class PositionController {

    @Resource
    private PositionService positionService;

    @GetMapping("/selectAll")
    public Result<List<Position>> selectAll(String jobCategoryId){
        return RestResultUtil.buildSuccessResult(positionService.selectAll(jobCategoryId),"查询成功");
    }

    @PostMapping("/add")
    public Result<String> add(Position position){
        int num = positionService.add(position);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("添加成功");
        }
        return RestResultUtil.buildFailResult("添加失败");
    }

    @PutMapping("/update")
    public Result<String> update(Position position){
        int num = positionService.update(position);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("修改成功");
        }
        return RestResultUtil.buildFailResult("修改失败");
    }


    @DeleteMapping("/delete")
    public Result<String> delete(String positionId){
        int num = positionService.delete(positionId);
        if (num == 1){
            return RestResultUtil.buildSuccessResult("删除成功");
        }
        return RestResultUtil.buildFailResult("删除失败");
    }
}
