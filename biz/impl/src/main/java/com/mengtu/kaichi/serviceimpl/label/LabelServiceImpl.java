package com.mengtu.kaichi.serviceimpl.label;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengtu.kaichi.label.mapper.LabelMapper;
import com.mengtu.kaichi.label.pojo.Label;
import com.mengtu.kaichi.serviceimpl.label.service.LabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelMapper labelMapper;

    @Override
    public List<Label> selectAll() {
        return labelMapper.selectList(new QueryWrapper<Label>().eq("ishidden","0"));
    }

    @Override
    public int deleteById(String labelId) {
        return labelMapper.deleteById(labelId);
    }

    @Override
    public int deleteByName(String labelName) {
        return labelMapper.delete(new QueryWrapper<Label>().eq("label_name",labelName));
    }

    @Override
    public int updateById(Label label) {
        return labelMapper.update(label,new QueryWrapper<Label>().eq("label_id",label.getLabelId()));
    }

//    @Override
//    public int updateByName(Label label) {
//        return labelMapper.update(label,new QueryWrapper<Label>().eq("label_name",label.getLabelId()));
//    }


    @Override
    public int addByName(String labelName) {
        Label label = new Label();
        label.setLabelName(labelName);
        return labelMapper.insert(label);
    }
}
