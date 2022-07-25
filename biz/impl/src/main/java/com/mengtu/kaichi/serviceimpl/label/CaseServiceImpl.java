package com.mengtu.kaichi.serviceimpl.label;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengtu.kaichi.label.mapper.CaseMapper;
import com.mengtu.kaichi.label.pojo.Case;
import com.mengtu.kaichi.serviceimpl.label.service.CaseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CaseServiceImpl implements CaseService {

    @Resource
    private CaseMapper caseMapper;

    @Override
    public List<Case> selectAll(String labelId) {
        return caseMapper.selectList(new QueryWrapper<Case>().eq("label_id",labelId));
    }

    @Override
    public int insert(Case c) {
        return caseMapper.insert(c);
    }

    @Override
    public int update(Case c) {
        return caseMapper.update(c,new QueryWrapper<Case>().eq("case_id",c.getCaseId()));
    }

    @Override
    public int delete(String caseId) {
        return caseMapper.delete(new QueryWrapper<Case>().eq("case_id",caseId));
    }
}
