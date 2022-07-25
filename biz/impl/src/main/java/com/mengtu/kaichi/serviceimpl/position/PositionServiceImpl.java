package com.mengtu.kaichi.serviceimpl.position;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengtu.kaichi.position.mapper.PositionMapper;
import com.mengtu.kaichi.position.pojo.Position;
import com.mengtu.kaichi.serviceimpl.position.service.PositionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Resource
    private PositionMapper positionMapper;

    @Override
    public List<Position> selectAll(String jobCategoryId) {
        return positionMapper.selectList(new QueryWrapper<Position>().eq("job_category_id",jobCategoryId));
    }

    @Override
    public int add(Position position) {
        return positionMapper.insert(position);
    }

    @Override
    public int update(Position position) {
        return positionMapper.updateById(position);
    }

    @Override
    public int delete(String positionId) {
        return positionMapper.deleteById(positionId);
    }
}
