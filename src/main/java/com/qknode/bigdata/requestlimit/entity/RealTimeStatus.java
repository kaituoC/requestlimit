package com.qknode.bigdata.requestlimit.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaituo
 * @date 2018-10-25
 */
@Data
public class RealTimeStatus {
    private String deviceId;
    private String imei;
    private String androidId;
    private Map<String, Integer> userClickMap = new HashMap<>();
    private Map<String, Integer> userInViewMap = new HashMap<>();
    private Map<String, Integer> dspClickMap = new HashMap<>();
    private Map<String, Integer> dspInViewMap = new HashMap<>();
}
