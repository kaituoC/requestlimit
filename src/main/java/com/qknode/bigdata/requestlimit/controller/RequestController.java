package com.qknode.bigdata.requestlimit.controller;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author kaituo
 * @date 2018-10-17
 */
@RestController
@RequestMapping(value = "/request")
public class RequestController {
    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    private RequestService requestService;

    @RequestMapping(value = "/get/splash")
    @ResponseBody
    public JSONObject get(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        List<JSONObject> data = requestService.process(request, AdType.SPLASH);
        result.put("data", data);
        result.put("code", 1000);
        result.put("msg", "ok");
        return result;
    }

}
