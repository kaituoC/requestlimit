package com.qknode.bigdata.requestlimit.service;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.Variables;
import com.qknode.bigdata.requestlimit.entity.RequestContent;
import com.qknode.bigdata.requestlimit.entity.RealTimeStatus;
import com.qknode.bigdata.requestlimit.limit.RequestLimit;
import com.qknode.bigdata.requestlimit.service.task.ITask;
import com.qknode.bigdata.requestlimit.service.task.ITaskA;
import com.qknode.bigdata.requestlimit.service.task.ITaskB;
import com.qknode.bigdata.requestlimit.service.task.ITaskC;
import com.qknode.bigdata.requestlimit.utils.VersionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author kaituo
 * @date 2018-10-17
 */
@Service
public class RequestService {
    private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

    @Autowired
    RequestLimit requestLimit;

    public LinkedList<JSONObject> process(HttpServletRequest request, AdType adType) {
        LinkedList<JSONObject> resultList = new LinkedList<>();
        if (request == null || request.getParameterMap().isEmpty()) {
            logger.error("request is null or empty!");
            return resultList;
        }
        RequestContent content = new RequestContent(request);
//        获取对应版本、对应广告类型可用的dsp列表
        List<String> onlineDsp = getOnlineDsp(content, adType);
        logger.info("onlineDsp={}", onlineDsp);
//        根据控制规则过滤dsp，得到可请求的dsp列表(规则对应数据存储在内存中，10分钟更新一次)
        List<String> requestableDsp = requestLimit.getRequestableDsp(onlineDsp, content);
        logger.info("requestableDsp={}", requestableDsp);
//        获取可请求dsp列表对应的task任务，并提交tasks
        List<ITask> tasks = getTasks(content, adType, requestableDsp);
        if (!tasks.isEmpty()) {
            for (ITask task : tasks) {
                task.submit();
            }
        } else {
            logger.error("tasks.isEmpty!!!");
        }
//        在tasks运行期间，获取对应用户的实时点击、曝光数据等实时规则数据
        RealTimeStatus realTimeStatus = getUserStatus(content, requestableDsp);
        logger.info("realTimeStatus={}", realTimeStatus.toString());
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
        if (resultList.size() < content.getRequireNum()) {
            fillAd(resultList, content);
        }

//        处理结果集数据(添加金币，设置空曝光等)，记录结果数据
        resultHandle(resultList, realTimeStatus);

        return resultList;
    }

    /**
     * 处理结果集
     *
     * @param resultList
     * @param realTimeStatus
     */
    private void resultHandle(LinkedList<JSONObject> resultList, RealTimeStatus realTimeStatus) {
        Random random = new Random();
        for (JSONObject jo : resultList) {
            String dsp = jo.getString("dsp");
            if (realTimeStatus.getUserClickMap().getOrDefault(dsp, 0) < Constants.DAILY_USER_CLICK_MIN_LIMIT) {
                if (random.nextInt(100) < 50) {
                    jo.put("coin", 5 + random.nextInt(5));
                } else {
                    jo.put("coin", 0);
                }
            } else if (realTimeStatus.getUserClickMap().getOrDefault(dsp, 0) > Constants.DAILY_USER_CLICK_MAX_LIMIT) {
                jo.put("backup", true);
            } else {
                if (realTimeStatus.getDspClickMap().get(dsp) * 10000 / realTimeStatus.getDspInViewMap().get(dsp) < Variables.getInstance().getDspConfigMap().get(dsp).getCtr()) {
                    if (random.nextInt(100) < 5) {
                        jo.put("coin", 5 + random.nextInt(5));
                    }
                } else {
                    jo.put("coin", 0);
                }
            }
        }
    }

    /**
     * 填充广告
     *
     * @param resultList
     * @param content
     */
    private void fillAd(LinkedList<JSONObject> resultList, RequestContent content) {

    }

    /**
     * 获取用户实时曝光、点击数据
     *
     * @param content
     * @param requestableDsp
     * @return
     */
    private RealTimeStatus getUserStatus(RequestContent content, List<String> requestableDsp) {
        Random random = new Random();
        RealTimeStatus realTimeStatus = new RealTimeStatus();
        realTimeStatus.setAndroidId(content.getAndroidId());
        realTimeStatus.setDeviceId(content.getDeviceId());
        realTimeStatus.setImei(content.getImei());
        for (String dsp : requestableDsp) {
            realTimeStatus.getDspInViewMap().put(dsp, random.nextInt(10));
            realTimeStatus.getDspClickMap().put(dsp, random.nextInt(10));
            realTimeStatus.getUserClickMap().put(dsp, random.nextInt(10));
            realTimeStatus.getUserInViewMap().put(dsp, random.nextInt(10));
        }
        return realTimeStatus;
    }

    /**
     * 给定 dsp 名称，获取 dsp task列表
     *
     * @param content
     * @param adType
     * @param dspList
     * @return
     */
    private List<ITask> getTasks(RequestContent content, AdType adType, List<String> dspList) {
        List<ITask> tasks = new LinkedList<>();
        if (!dspList.isEmpty()) {
            for (String dsp : dspList) {
                if (Constants.A.equals(dsp)) {
                    tasks.add(new ITaskA(content, adType));
                } else if (Constants.B.equals(dsp)) {
                    tasks.add(new ITaskB(content, adType));
                } else if (Constants.C.equals(dsp)) {
                    tasks.add(new ITaskC(content, adType));
                }
            }
        } else {
            logger.error("dspList is empty!");
        }
        return tasks;
    }

    /**
     * 获取对应版本的可用dsp列表
     *
     * @param content
     * @param adType
     * @return
     */
    private List<String> getOnlineDsp(RequestContent content, AdType adType) {
        List<String> onlineDsp = new LinkedList<>();
        if (AdType.SPLASH.equals(adType)) {
            if (VersionUtils.compareVersion(content.getVerCode(), "1.5.3") >= 0) {
                onlineDsp = Constants.ONLINE_DSP;
            }
        } else if (AdType.BANNER.equals(adType)) {

        } else {

        }
        return onlineDsp;
    }
}
