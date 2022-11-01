package com.mengtu.kaichi.serviceimpl.pm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengtu.kaichi.pm.mapper.PmMapper;
import com.mengtu.kaichi.pm.pojo.Pm;
import com.mengtu.kaichi.position.pojo.Position;
import com.mengtu.kaichi.serviceimpl.pm.service.PmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PmServiceImpl implements PmService {

    @Resource
    private PmMapper pmMapper;


    @Override
    public List<Pm> selectAll(Pm pm) {
        return pmMapper.selectList(new QueryWrapper<Pm>().eq("initial_id",pm.getInitialId()));
    }

    @Override
    public int add(Pm pm) {
        return pmMapper.insert(pm);
    }

    @Override
    public int update(Pm pm) {
        return pmMapper.updateById(pm);
    }

    @Override
    public int delete(Pm pm) {
        return pmMapper.deleteById(pm.getPmId());
    }

    @Override
    public Pm selectById(Pm pm) {
        return pmMapper.selectById(pm.getPmId());
    }

    @Override
    public Pm duplicate(Pm pm) {
        pm = pmMapper.selectById(pm.getPmId());
        pm.setProjectName(pm.getProjectName()+"-副本");
        pm.setPmId(null);
        pmMapper.insert(pm);
        return pm;
    }
}
