package com.qknode.bigdata.requestlimit.commant;

import com.qknode.bigdata.requestlimit.entity.DspConfig;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author kaituo
 * @date 2018-10-25
 */
public class Variables {
    private static Variables instance = null;
    private List<String> onlineDspList = new LinkedList<>();
    private Map<String, DspConfig> dspConfigMap = new HashMap<>();

    public static Variables getInstance() {
        if (instance==null) {
            synchronized (Variables.class) {
                if (instance==null) {
                    instance = new Variables();
                }
            }
        }
        return instance;
    }

    public List<String> getOnlineDspList() {
        return onlineDspList;
    }

    public void setOnlineDspList(List<String> onlineDspList) {
        this.onlineDspList = onlineDspList;
    }

    public Map<String, DspConfig> getDspConfigMap() {
        return dspConfigMap;
    }

    public void setDspConfigMap(Map<String, DspConfig> dspConfigMap) {
        this.dspConfigMap = dspConfigMap;
    }
}
