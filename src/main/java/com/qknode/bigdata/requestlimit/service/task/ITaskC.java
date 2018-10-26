package com.qknode.bigdata.requestlimit.service.task;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.entity.RequestContent;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author kaituo
 * @date 2018-10-18
 */
public class ITaskC implements ITask {
    private List<JSONObject> resultList = new LinkedList<>();
    private RequestContent content;
    private AdType adType;
    public ITaskC(RequestContent content, AdType adType) {
        this.content = content;
        this.adType = adType;
    }

    @Override
    public void submit() {
        JSONObject jo = new JSONObject();
        jo.put("source", Constants.B);
        jo.put("adType", this.adType);
        resultList.add(jo);
    }

    @Override
    public Optional<List<JSONObject>> apply() {
        return Optional.of(resultList);
    }
}
