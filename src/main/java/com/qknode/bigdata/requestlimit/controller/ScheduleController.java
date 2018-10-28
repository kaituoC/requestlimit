package com.qknode.bigdata.requestlimit.controller;

import com.qknode.bigdata.requestlimit.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kaituo
 * @date 2018-10-25
 */
@RestController
public class ScheduleController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleController.class);

    @Autowired
    ScheduleService scheduleService;

    /**
     * 高频调度器，每 1 分钟执行一次
     */
    @Scheduled(initialDelay = 0, fixedDelay = 60000)
    public void highFrequencySchedule() {
        scheduleService.loadCacheIntoRedis();
        logger.info("highFrequencySchedule run over!");
    }

    /**
     * 中频调度器，每 5 分钟执行一次
     */
//    @Scheduled(initialDelay = 0, fixedDelay = 300000)
    public void mediumFrequencySchedule() {
        logger.info("mediumFrequencySchedule run over!");
    }

    /**
     * 低频调度器，每 10 分钟执行一次
     */
    @Scheduled(initialDelay = 0, fixedDelay = 600000)
    public void lowFrequencySchedule() {
        scheduleService.loadDspConfig();
        logger.info("lowFrequencySchedule run over!");
    }
}
