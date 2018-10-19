package com.qknode.bigdata.requestlimit.service;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.FeedAdType;
import com.qknode.bigdata.requestlimit.entity.RequestContent;
import com.qknode.bigdata.requestlimit.limit.RequestLimit;
import com.qknode.bigdata.requestlimit.service.task.ITask;
import com.qknode.bigdata.requestlimit.service.task.ITaskA;
import com.qknode.bigdata.requestlimit.service.task.ITaskB;
import com.qknode.bigdata.requestlimit.service.task.ITaskC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * @author kaituo
 * @date 2018-10-17
 */
@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    RequestLimit requestLimit;

    public LinkedList<JSONObject> process(HttpServletRequest request, AdType adType, FeedAdType feedAdType) {
        RequestContent content = new RequestContent(request);
        LinkedList<JSONObject> resultList = new LinkedList<>();
//        获取对应版本、对应广告类型可用的dsp列表
        List<String> onlineDsp = getOnlineDsp(content, adType, feedAdType);
//        根据控制规则过滤dsp，得到可请求的dsp列表(规则对应数据存储在内存中，10分钟更新一次)
        List<String> requestableDsp = requestLimit.getRequestableDsp(onlineDsp, content);
//        获取可请求dsp列表对应的task任务，并提交tasks
        List<ITask> tasks = getTasks(content, adType, feedAdType, onlineDsp);
        if (!tasks.isEmpty()) {
            for (ITask task : tasks) {
                task.submit();
            }
        } else {
            logger.error("tasks.isEmpty!!!");
        }
//        在tasks运行期间，获取对应用户的实时点击、曝光数据等实时规则数据

//        获取广告tasks结果集
        for (ITask task : tasks) {
            Optional<List<JSONObject>> apply = task.apply();
            if (apply.isPresent()) {
                List<JSONObject> entries = apply.get();
                if (!entries.isEmpty()) {
                    resultList.addAll(entries);
                } else {
                    logger.error("adEntries.isEmpty");
                }
            } else {
                logger.error("apply is not present!");
            }
        }
//        根据结果集个数填补空余广告位

//        处理结果集数据(添加金币，设置空曝光等)，记录结果数据

        return resultList;
    }

    private List<ITask> getTasks(RequestContent content, AdType adType, FeedAdType feedAdType, List<String> onlineDsp) {
        List<ITask> tasks = new LinkedList<>();
        for (String dsp : onlineDsp) {
            if (Constants.A.equalsIgnoreCase(dsp)) {
                tasks.add(new ITaskA(content, adType, feedAdType));
            } else if (Constants.B.equalsIgnoreCase(dsp)) {
                tasks.add(new ITaskB(content, adType, feedAdType));
            } else if (Constants.C.equalsIgnoreCase(dsp)) {
                tasks.add(new ITaskC(content, adType, feedAdType));
            }
        }
        return tasks;
    }

    private List<String> getOnlineDsp(RequestContent content, AdType adType, FeedAdType feedAdType) {
        List<String> onlineDsp = Collections.emptyList();
        if (AdType.SPLASH.equals(adType)) {
            if ("1.5.3".equals(content.getVerCode())) {
                onlineDsp = Constants.ONLINE_DSP;
            }
        } else if (AdType.BANNER.equals(adType)) {

        } else {

        }
        return onlineDsp;
    }
}
