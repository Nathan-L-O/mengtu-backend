package com.mengtu.kaichi.serviceimpl.position.service;



import com.mengtu.kaichi.position.pojo.LinkPointFolder;
import com.mengtu.kaichi.position.pojo.Position;

import java.util.List;

public interface LinkPointFolderService {

    /*根据类型id查找所有*/
    int insert(LinkPointFolder linkPointFolder);

    List<LinkPointFolder> queryAll();

    int updateByFolderId(String folderId);

    int update(LinkPointFolder linkPointFolder);

}
