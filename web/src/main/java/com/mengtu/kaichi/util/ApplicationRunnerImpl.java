package com.mengtu.kaichi.util;

import com.mengtu.util.audit.SensitiveFilterUtil;
import com.mengtu.util.storage.ObsUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 实现 ApplicationRunner 接口
 *
 * @author 过昊天
 * @version 1.0 @ 2022/4/22 11:22
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Resource
    ObsUtil obsUtil;

    @Resource
    SensitiveFilterUtil sensitiveFilterUtil;

    @Override
    public void run(ApplicationArguments args) {
        obsUtil.getInstance();
        sensitiveFilterUtil.getInstance(obsUtil.download(obsUtil.fetchFile("adult/", "SensitiveFilter")));
    }

}
