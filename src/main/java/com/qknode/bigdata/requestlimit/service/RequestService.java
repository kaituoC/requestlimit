package com.qknode.bigdata.requestlimit.service;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kaituo
 * @date 2018-10-17
 */
@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    public JSONObject process(HttpServletRequest request) {

        return null;
    }
}
