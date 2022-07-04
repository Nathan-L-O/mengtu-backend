package com.mengtu;

import com.mengtu.kaichi.serviceimpl.common.init.InitService;
import com.mengtu.util.tools.DateUtil;
import com.mengtu.util.tools.LoggerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.TimeZone;

/**
 * SpringbootApplication 入口
 *
 * @author 过昊天
 * @version 1.1 @ 2022/6/6 14:01
 */
@EnableScheduling
@EnableJpaAuditing
@EnableAspectJAutoProxy
@EnableTransactionManagement
@ImportResource(locations = {
        "classpath:spring/biz-service.xml",
        "classpath:spring/user.xml",
        "classpath:spring/organization.xml"})
@SpringBootApplication
public class KaichiWebApplication {

    @Resource
    private InitService initService;

    private static KaichiWebApplication kaichiWebApplication;


    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        SpringApplication.run(KaichiWebApplication.class, args);
        kaichiWebApplication.initService.init();

        LoggerUtil.info(LoggerFactory.getLogger(KaichiWebApplication.class),
                "Starting backend service for MengTu-Tech, current time is {0}",
                DateUtil.format(new Date(), "yyyy/MM/dd - HH:mm:ss"));
    }

    @PostConstruct
    private void init() {
        kaichiWebApplication = this;
        kaichiWebApplication.initService = this.initService;
    }

}
