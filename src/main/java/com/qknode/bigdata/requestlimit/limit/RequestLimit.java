package com.qknode.bigdata.requestlimit.limit;

import com.google.common.cache.Cache;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.entity.MemCache;
import com.qknode.bigdata.requestlimit.entity.RequestContent;
import com.qknode.bigdata.requestlimit.utils.TimeUtil;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author kaituo
 * @date 2018-10-17
 */
@Component
public class RequestLimit {

    public List<String> getRequestableDsp(List<String> onlineDsp, RequestContent content) {
        List<String> requestableDsp = new LinkedList<>();
        long timeStamp = System.currentTimeMillis();
        String today = TimeUtil.getD(timeStamp);
        String hour = TimeUtil.getH(timeStamp);
        for (String dsp : onlineDsp) {
            String dayKey = Constants.DSP_REQUEST_PREFIX + today  + ":" + dsp + ":" + content.getDeviceId();
            String hourKey = Constants.DSP_REQUEST_PREFIX + hour  + ":" + dsp + ":" + content.getDeviceId();
            if (!isReachLimit(dayKey, Constants.REQUEST_LIMIT.get(dsp)) && isReachLimit(hourKey, Constants.REQUEST_LIMIT.get(dsp))) {
                requestableDsp.add(dsp);
            }
        }
        return requestableDsp;
    }

    /**
     * 判断是否达到上限
     * @param key key
     * @param topLimit 大于 0 的整数
     * @return true: 达到上限
     */
    public boolean isReachLimit(String key, Long topLimit) {
        boolean status = false;
        Cache<String, Long> limitCache = MemCache.getInstance().getLimitCache();
        Long value = limitCache.getIfPresent(key);
        if (value == null) {
            limitCache.put(key, 1L);
        } else {
            status = (value >= topLimit);
            limitCache.put(key, value + 1);
        }
        return status;
    }
}
