package com.qknode.bigdata.requestlimit.service;

import com.google.common.collect.Lists;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.Variables;
import com.qknode.bigdata.requestlimit.entity.DspConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author kaituo
 * @date 2018-10-28
 */
@Service
public class ScheduleService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    public boolean loadDspConfig() {
        boolean status = false;
        try {
            Variables variables = Variables.getInstance();
            List<String> onlineDspList = new LinkedList<>();
            onlineDspList.add(Constants.A);
            onlineDspList.add(Constants.B);
            onlineDspList.add(Constants.C);
            variables.setOnlineDspList(onlineDspList);
            Map<String, DspConfig> dspConfigMap = new HashMap<>();
            List<String> androidExcludeVersionList = Lists.newArrayList("1.5.4", "1.6.0");
            List<String> iosExcludeVersionList = Lists.newArrayList("1.5.4", "1.6.0");
            dspConfigMap.put(Constants.A, new DspConfig(Constants.A, "1.5.3", androidExcludeVersionList, "1.5.0", iosExcludeVersionList, 100L, 10L, 100L, 10L, 300, "Y", "Y"));
            dspConfigMap.put(Constants.B, new DspConfig(Constants.B, "1.5.3", androidExcludeVersionList, "1.5.0", iosExcludeVersionList, 150L, 15L, 150L, 15L, 350, "Y", "Y"));
            dspConfigMap.put(Constants.C, new DspConfig(Constants.C, "1.5.3", androidExcludeVersionList, "1.5.0", iosExcludeVersionList, 200L, 20L, 200L, 20L, 280, "Y", "Y"));
            variables.setDspConfigMap(dspConfigMap);
            logger.info("load dspConfig={}", dspConfigMap);
            status = true;
        } catch (Exception e) {
            logger.error("loadDspConfig failed!");
        }
        return status;
    }

    public boolean loadCacheIntoRedis() {
        boolean status = false;

        return status;
    }
}
