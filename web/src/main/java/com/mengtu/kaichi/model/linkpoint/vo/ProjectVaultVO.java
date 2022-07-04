package com.mengtu.kaichi.model.linkpoint.vo;

import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;

import java.util.ArrayList;
import java.util.List;

/**
 * 库视图对象
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/1 09:46
 */
public class ProjectVaultVO extends ProjectVaultBO {

    private static final long serialVersionUID = 5043538601971468219L;

    /**
     * path
     */
    private String path;

    /**
     * hashtag List
     */
    public List<String> hashtagList = new ArrayList<>();

    public String getPath() {
        return path;
    }


    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getHashtagList() {
        return hashtagList;
    }

    public void setHashtagList(List<String> hashtagList) {
        this.hashtagList = hashtagList;
    }

    public void putHashtagList(String key) {
        if (hashtagList == null) {
            hashtagList = new ArrayList<>();
        }
        if (!hashtagList.contains(key)) {
            hashtagList.add(key);
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}