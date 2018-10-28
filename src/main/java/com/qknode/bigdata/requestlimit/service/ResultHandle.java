package com.qknode.bigdata.requestlimit.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.Variables;
import com.qknode.bigdata.requestlimit.entity.DspConfig;
import com.qknode.bigdata.requestlimit.entity.RealTimeStatus;
import com.qknode.bigdata.requestlimit.entity.RequestContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * @author changkaituo
 * @date 2018/10/28
 */
@Component
public class ResultHandle {
    private static final Logger logger = LoggerFactory.getLogger(ResultHandle.class);

    /**
     * 处理广告数据集
     * @param dataList
     * @param realTimeStatus
     * @param content
     * @param adType
     * @return
     */
    public List<JSONObject> handle(List<JSONObject> dataList, RealTimeStatus realTimeStatus, RequestContent content, AdType adType) {
//        List<JSONObject> resultList = new LinkedList<>();
        List<JSONObject> resultList = Lists.newLinkedList(dataList);
        Random random = new Random();
        for (JSONObject jo : resultList) {
            String dsp = jo.getString("source");
            int clickNum = realTimeStatus.getUserClickMap().getOrDefault(dsp, 0);
            int inViewNum = realTimeStatus.getUserClickMap().getOrDefault(dsp, 0);
            DspConfig dspConfig = Variables.getInstance().getDspConfigMap().get(dsp);
            if (clickNum < Constants.DAILY_USER_CLICK_MIN_LIMIT) {
                if (random.nextInt(100) < 50) {
                    jo.put("coin", 5 + random.nextInt(5));
                    logger.info("1.0 add coin,dsp={} coin={}", dsp, jo.getInteger("coin"));
                } else {
                    jo.put("coin", 0);
                    logger.info("1.5 add coin,dsp={} coin={}", dsp, jo.getInteger("coin"));
                }
            } else if (clickNum > Constants.DAILY_USER_CLICK_MAX_LIMIT) {
                jo.put("backup", true);
                logger.info("2.0 add coin,dsp={} backup={}", dsp, jo.getBoolean("backup"));
            } else {
                if (clickNum * 10000 / inViewNum < dspConfig.getCtr()) {
                    if (random.nextInt(100) < 5) {
                        jo.put("coin", 5 + random.nextInt(5));
                        logger.info("3.0 add coin,dsp={} coin={}", dsp, jo.getInteger("coin"));
                    } else {
                        jo.put("coin", 0);
                        logger.info("3.5 add coin,dsp={} coin={}", dsp, jo.getInteger("coin"));
                    }
                } else {
                    jo.put("coin", 0);
                    logger.info("4.0 add coin,dsp={} coin={}", dsp, jo.getInteger("coin"));
                }
            }
        }
        return resultList;
    }

}
