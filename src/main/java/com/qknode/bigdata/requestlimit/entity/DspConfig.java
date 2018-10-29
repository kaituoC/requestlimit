package com.qknode.bigdata.requestlimit.entity;

import lombok.Data;

import java.util.List;

/**
 * @author kaituo
 * @date 2018-10-25
 */
@Data
public class DspConfig {
    private String dsp;
    private String androidMinVersion;
    private List<String> androidExcludeVersionList;
    private String iosMinVersion;
    private List<String> iosExcludeVersionList;
    private long dayLimit;
    private long hourLimit;
    private long dayLimitPerUser;
    private long hourLimitPerUser;
    /**
     * 单位是万分之，123表示 123/10000=1.23%
     */
    private int ctr;
    /**
     * Y : 输出详细日志
     * N : 只输出错误日志
     */
    private String logLevel;
    /**
     * Y : 可以作为填充广告
     * N : 不可以作为填充广告
     */
    private String isFillAble;
    /**
     * Y : 可以作为填充广告
     * N : 不可以作为填充广告
     */
    private String needProcess;

    public DspConfig() {

    }

    public DspConfig(String dsp, String androidMinVersion, List<String> androidExcludeVersionList, String iosMinVersion, List<String> iosExcludeVersionList, long dayLimit, long hourLimit, long dayLimitPerUser, long hourLimitPerUser, int ctr, String logLevel, String isFillAble, String needProcess) {
        this.dsp = dsp;
        this.androidMinVersion = androidMinVersion;
        this.androidExcludeVersionList = androidExcludeVersionList;
        this.iosMinVersion = iosMinVersion;
        this.iosExcludeVersionList = iosExcludeVersionList;
        this.dayLimit = dayLimit;
        this.hourLimit = hourLimit;
        this.dayLimitPerUser = dayLimitPerUser;
        this.hourLimitPerUser = hourLimitPerUser;
        this.ctr = ctr;
        this.logLevel = logLevel;
        this.isFillAble = isFillAble;
        this.needProcess = needProcess;
    }
}
