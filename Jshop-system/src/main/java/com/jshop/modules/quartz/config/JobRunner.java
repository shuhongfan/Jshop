/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.kaikeba.co

 */
package com.jshop.modules.quartz.config;

import com.jshop.modules.quartz.domain.QuartzJob;
import com.jshop.modules.quartz.service.QuartzJobService;
import com.jshop.modules.quartz.utils.QuartzManage;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author jack胡
 */
@Component
public class JobRunner implements ApplicationRunner {

    private final QuartzJobService quartzJobService;

    private final QuartzManage quartzManage;

    public JobRunner(QuartzJobService quartzJobService, QuartzManage quartzManage) {
        this.quartzJobService = quartzJobService;
        this.quartzManage = quartzManage;
    }

    /**
     * 项目启动时重新激活启用的定时任务
     * @param applicationArguments /
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        System.out.println("--------------------注入定时任务---------------------");
        List<QuartzJob> quartzJobs = quartzJobService.findByIsPauseIsFalse();
        quartzJobs.forEach(quartzManage::addJob);
        System.out.println("--------------------定时任务注入完成---------------------");
    }
}
