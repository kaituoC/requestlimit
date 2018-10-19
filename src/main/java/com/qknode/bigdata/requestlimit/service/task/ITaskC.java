package com.qknode.bigdata.requestlimit.service.task;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.FeedAdType;
import com.qknode.bigdata.requestlimit.entity.RequestContent;

import java.util.List;
import java.util.Optional;

/**
 * @author kaituo
 * @date 2018-10-18
 */
public class ITaskC implements ITask {
    public ITaskC(RequestContent content, AdType adType, FeedAdType feedAdType) {

    }

    @Override
    public void submit() {

    }

    @Override
    public Optional<List<JSONObject>> apply() {
        return Optional.empty();
    }
}
