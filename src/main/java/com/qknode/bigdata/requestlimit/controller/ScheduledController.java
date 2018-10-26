package com.qknode.bigdata.requestlimit.controller;

import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.Variables;
import com.qknode.bigdata.requestlimit.entity.DspConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author kaituo
 * @date 2018-10-25
 */
@RestController
public class ScheduledController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledController.class);

    /**
     * 每十分钟重新 reload 分享热文
     */
    @Scheduled(initialDelay = 0, fixedDelay = 600000)
    public void loadDspConfig() {
        Variables variables = Variables.getInstance();
        List<String> onlineDspList = new LinkedList<>();
        onlineDspList.add(Constants.A);
        onlineDspList.add(Constants.B);
        onlineDspList.add(Constants.C);
        variables.setOnlineDspList(onlineDspList);
        Map<String, DspConfig> dspConfigMap = new HashMap<>();
        dspConfigMap.put(Constants.A, new DspConfig(Constants.A, "1.5.3", "1.5.0", 100L, 10L, 100L, 10L, 300));
        dspConfigMap.put(Constants.B, new DspConfig(Constants.B, "1.5.3", "1.5.0", 150L, 15L, 150L, 15L, 350));
        dspConfigMap.put(Constants.C, new DspConfig(Constants.C, "1.5.3", "1.5.0", 200L, 20L, 200L, 20L, 280));
        variables.setDspConfigMap(dspConfigMap);
        logger.info("load dspConfig={}", dspConfigMap);
    }
}
