package com.qknode.bigdata.requestlimit.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaituo
 * @date 2018-10-18
 */
@Data
public class RequestContent {
    private String deviceId;
    private String androidId;
    private String imei;
    private String verCode;
    private int requireNum;

    public RequestContent(HttpServletRequest request) {
        this.deviceId = StringUtils.defaultString(request.getParameter("deviceId"), "");
        this.androidId = StringUtils.defaultString(request.getParameter("androidId"), "");
        this.imei = StringUtils.defaultString(request.getParameter("imei"), "");
        this.verCode = StringUtils.defaultString(request.getParameter("verCode"), "");
        String numStr = StringUtils.defaultString(request.getParameter("requireNum"), "1");
        if (NumberUtils.isParsable(numStr)) {
            this.requireNum = NumberUtils.createInteger(numStr);
        } else {
            this.requireNum = 1;
        }
    }
}
