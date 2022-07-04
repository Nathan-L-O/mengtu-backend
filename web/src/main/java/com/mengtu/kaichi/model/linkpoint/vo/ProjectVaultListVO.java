package com.mengtu.kaichi.model.linkpoint.vo;

import com.mengtu.util.common.ToString;

import java.util.List;

/**
 * 库视图列表对象
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/26 17:52
 */
public class ProjectVaultListVO extends ToString {

    private static final long serialVersionUID = -5419890794085702363L;

    private String path;

    private List<ProjectVaultVO> projectVaultVOList;

    private List<ProjectVaultListVO> childNodes;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ProjectVaultVO> getProjectVaultVOList() {
        return projectVaultVOList;
    }

    public void setProjectVaultVOList(List<ProjectVaultVO> projectVaultVOList) {
        this.projectVaultVOList = projectVaultVOList;
    }

    public List<ProjectVaultListVO> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<ProjectVaultListVO> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}