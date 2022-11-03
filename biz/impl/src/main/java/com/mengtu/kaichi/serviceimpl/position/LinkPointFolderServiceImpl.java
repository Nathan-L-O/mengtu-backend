package com.mengtu.kaichi.serviceimpl.position;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengtu.kaichi.position.mapper.LinkPointFolderMapper;
import com.mengtu.kaichi.position.pojo.LinkPointFolder;
import com.mengtu.kaichi.serviceimpl.position.service.LinkPointFolderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LinkPointFolderServiceImpl implements LinkPointFolderService {

    @Resource
    private LinkPointFolderMapper linkPointFolderMapper;

    @Override
    public int insert(LinkPointFolder linkPointFolder) {
        return linkPointFolderMapper.insert(linkPointFolder);
    }

    @Override
    public List<LinkPointFolder> queryAll() {
        return linkPointFolderMapper.selectList(null);
    }

    @Override
    public int updateByFolderId(String folderId) {
        LinkPointFolder linkPointFolder = new LinkPointFolder();
        linkPointFolder.setId(folderId);
        return linkPointFolderMapper.updateById(linkPointFolder);
    }
}
