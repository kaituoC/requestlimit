package com.qknode.bigdata.requestlimit.service.task;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Optional;

/**
 * @author kaituo
 * @date 2018-10-18
 */
public interface ITask {

    void submit();
    Optional<List<JSONObject>> apply();

}
