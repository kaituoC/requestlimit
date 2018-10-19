package com.qknode.bigdata.requestlimit.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaituo
 * @date 2018-10-18
 */
@Data
public class RequestContent {
    private String deviceId;
    private String verCode;

    public RequestContent(HttpServletRequest request) {
        this.deviceId = StringUtils.defaultString(request.getParameter("deviceId"), "");
        this.verCode = StringUtils.defaultString(request.getParameter("verCode"), "");
    }
}
