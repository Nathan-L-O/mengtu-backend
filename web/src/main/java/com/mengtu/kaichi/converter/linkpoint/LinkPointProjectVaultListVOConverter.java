package com.mengtu.kaichi.converter.linkpoint;

import com.mengtu.kaichi.linkpoint.model.ProjectVaultBO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVaultListVO;
import com.mengtu.kaichi.model.linkpoint.vo.ProjectVaultVO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 项目模型转换器
 *
 * @author 过昊天
 * @version 1.0 @ 2022/5/10 14:36
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
final public class LinkPointProjectVaultListVOConverter {

    private static final String SORT_FLAG = "sort";

    private static final String SEPARATOR = "/";

    public static ProjectVaultListVO convert(List<ProjectVaultBO> projectVaultBOList) {
        if (projectVaultBOList == null) {
            return null;
        }

        //TODO: 二层结构暂时使用判据遍历实现，后期需要改为递归生成树
        Map<String, ProjectVaultListVO> flagMap = new ConcurrentHashMap<>(0);

        for (ProjectVaultBO projectVaultBO : projectVaultBOList) {
            String uri = projectVaultBO.getModelName();
            String path = uri.substring(0, uri.lastIndexOf(SEPARATOR) + 1);
            ProjectVaultListVO projectVaultListVO;

            if (flagMap.containsKey(path)) {
                projectVaultListVO = flagMap.get(path);

                List<ProjectVaultVO> projectVaultVOList = projectVaultListVO.getProjectVaultVOList();
                projectVaultVOList.add(LinkPointProjectVaultVOConverter.convert(projectVaultBO));
                projectVaultListVO.setProjectVaultVOList(projectVaultVOList);

            } else {
                projectVaultListVO = new ProjectVaultListVO();
                projectVaultListVO.setPath(path);

                List<ProjectVaultVO> projectVaultVOList = new ArrayList<>();
                projectVaultVOList.add(LinkPointProjectVaultVOConverter.convert(projectVaultBO));
                projectVaultListVO.setProjectVaultVOList(projectVaultVOList);

            }
            flagMap.put(path, projectVaultListVO);
        }

        ProjectVaultListVO ret = new ProjectVaultListVO();
        ret.setPath(SEPARATOR);
        List<ProjectVaultListVO> childNodes = new ArrayList<>();
        Map<String, ProjectVaultListVO> judgeMap = new ConcurrentHashMap<>(1);

        for (Map.Entry<String, ProjectVaultListVO> entry : flagMap.entrySet()) {
            String path = getFirst(entry.getKey());
            if (!judgeMap.containsKey(path)) {
                ProjectVaultListVO tmp = new ProjectVaultListVO();
                tmp.setPath(path);
                judgeMap.put(path, tmp);
            }
        }
        for (Map.Entry<String, ProjectVaultListVO> entry : flagMap.entrySet()) {
            String path = getSecond(entry.getKey());
            ProjectVaultListVO tmp = new ProjectVaultListVO();
            tmp.setPath(path);
            List<ProjectVaultVO> sortInstance = entry.getValue().getProjectVaultVOList();
            sortInstance.sort(Comparator.comparingInt(o -> Integer.parseInt(o.fetchExtInfo(SORT_FLAG))));
            tmp.setProjectVaultVOList(sortInstance);

            ProjectVaultListVO uplink = judgeMap.get(getFirst(entry.getKey()));
            List<ProjectVaultListVO> projectVaultListVOList = uplink.getChildNodes();
            if (projectVaultListVOList == null) {
                projectVaultListVOList = new ArrayList<>();
            }
            projectVaultListVOList.add(tmp);
            uplink.setChildNodes(projectVaultListVOList);
            judgeMap.put(getFirst(entry.getKey()), uplink);
        }

        for (Map.Entry<String, ProjectVaultListVO> entry : judgeMap.entrySet()) {
            childNodes.add(entry.getValue());
        }
        ret.setChildNodes(childNodes);

        return ret;
    }

    public static int getHierarchy(String uri) {
        int hierarchy = -1;
        while (uri.contains(SEPARATOR)) {
            hierarchy++;
            uri = uri.substring(uri.indexOf(SEPARATOR) + 1);
        }
        return hierarchy;
    }

    private static String getFirst(String uri) {
        uri = uri.substring(uri.indexOf(SEPARATOR) + 1);
        return uri.substring(0, uri.indexOf(SEPARATOR) + 1);
    }

    private static String getSecond(String uri) {
        uri = uri.substring(uri.indexOf(SEPARATOR) + 1);
        return uri.substring(uri.indexOf(SEPARATOR) + 1, uri.lastIndexOf(SEPARATOR) + 1);
    }
}
