package com.mengtu.kaichi.controller.linkpoint;

import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectRepoService;
import com.mengtu.kaichi.linkpoint.dal.service.LinkpointProjectVersionRepoService;
import com.mengtu.kaichi.linkpoint.model.ProjectVersionBO;
import com.mengtu.util.storage.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.Objects;


@CrossOrigin
@Controller
@ResponseBody
@RequestMapping(value = "/file", produces = {"application/json;charset=UTF-8"})
public class FileController {

    @Resource
    private LinkpointProjectVersionRepoService linkpointProjectVersionRepoService;

    @GetMapping
    public void getProjectInstance(HttpServletResponse response,
                                   @RequestParam(value = "path") String path,
                                   @RequestParam(value = "key") String key) throws FileNotFoundException, IOException {
        List<ProjectVersionBO> projectVersionBOList = linkpointProjectVersionRepoService.queryByProjectId(key);
        ProjectVersionBO projectVersionBO = null;
        if (!projectVersionBOList.isEmpty()) {
            projectVersionBO = projectVersionBOList.get(projectVersionBOList.size() - 1);
        }
        System.out.println(Objects.requireNonNull(projectVersionBO).getResourceUri());
        File temFile = FileUtil.getFile(path, Objects.requireNonNull(projectVersionBO).getResourceUri());
//        File temFile = FileUtil.getFile(path, key);

        response.setHeader("Content-Type", "text/html;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=data"+getFileExtension(temFile));
        response.setHeader("Content-Length", ""+ Objects.requireNonNull(temFile).length());

        FileCopyUtils.copy(new FileInputStream(temFile), response.getOutputStream());
    }

    public String getFileExtension(File file) {
        String extension = "";

        try {
            if (file != null && file.exists()) {
                String name = file.getName();

                extension = name.substring(name.lastIndexOf("."));

            }

        } catch (Exception e) {
            extension = "";

        }

        return extension;

    }

}
