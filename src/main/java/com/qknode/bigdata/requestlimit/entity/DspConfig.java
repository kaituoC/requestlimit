package com.qknode.bigdata.requestlimit.entity;

import lombok.Data;

/**
 * @author kaituo
 * @date 2018-10-25
 */
@Data
public class DspConfig {
    private String dsp;
    private String androidMinVersion;
    private String iosMinVersion;
    private long dayLimit;
    private long hourLimit;
    private long dayLimitPerUser;
    private long hourLimitPerUser;
    /**
     * 单位是万分之，123表示 123/10000=1.23%
     */
    private int ctr;

    public DspConfig() {

    }

    public DspConfig(String dsp, String androidMinVersion, String iosMinVersion, long dayLimit, long hourLimit, long dayLimitPerUser, long hourLimitPerUser, int ctr) {
        this.dsp = dsp;
        this.androidMinVersion = androidMinVersion;
        this.iosMinVersion = iosMinVersion;
        this.dayLimit = dayLimit;
        this.hourLimit = hourLimit;
        this.dayLimitPerUser = dayLimitPerUser;
        this.hourLimitPerUser = hourLimitPerUser;
        this.ctr = ctr;
    }
}
