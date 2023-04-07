package com.mengtu.kaichi.serviceimpl.position;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mengtu.kaichi.position.mapper.LinkPointFolderMapper;
import com.mengtu.kaichi.position.pojo.LinkPointFolder;
import com.mengtu.kaichi.serviceimpl.position.service.LinkPointFolderService;
import com.mengtu.util.enums.RestResultCode;
import com.mengtu.util.exception.KaiChiException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LinkPointFolderServiceImpl implements LinkPointFolderService {

    @Resource
    private LinkPointFolderMapper linkPointFolderMapper;

    @Override
    public int insert(LinkPointFolder linkPointFolder) {
        List<LinkPointFolder> linkPointFolders = linkPointFolderMapper.selectList(new QueryWrapper<LinkPointFolder>().eq("folder_name",linkPointFolder.getFolderName()));
        if (linkPointFolders.size()>0)
            throw new KaiChiException(RestResultCode.ILLEGAL_PARAMETERS, "文件名重复");
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

    @Override
    public int update(LinkPointFolder linkPointFolder) {
        List<LinkPointFolder> linkPointFolders = linkPointFolderMapper.selectList(new QueryWrapper<LinkPointFolder>().eq("folder_name",linkPointFolder.getFolderName()));
        if (linkPointFolders.size()>0)
            throw new KaiChiException(RestResultCode.ILLEGAL_PARAMETERS, "文件名重复");
        return linkPointFolderMapper.updateById(linkPointFolder);
    }
}
