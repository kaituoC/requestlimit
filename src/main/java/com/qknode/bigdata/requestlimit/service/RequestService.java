package com.qknode.bigdata.requestlimit.service;

import com.alibaba.fastjson.JSONObject;
import com.qknode.bigdata.requestlimit.commant.AdType;
import com.qknode.bigdata.requestlimit.commant.Constants;
import com.qknode.bigdata.requestlimit.commant.Variables;
import com.qknode.bigdata.requestlimit.entity.DspConfig;
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
    private int tempCount = 0;

    @Autowired
    RequestLimit requestLimit;

    @Autowired
    ResultHandle resultHandle;

    public List<JSONObject> process(HttpServletRequest request, AdType adType) {
        List<JSONObject> dataList = new LinkedList<>();
        if (request == null || request.getParameterMap().isEmpty()) {
            logger.error("request is null or empty!");
            return dataList;
        }
        RequestContent content = new RequestContent(request);
//        获取对应版本、对应广告类型可用的dsp列表
        List<String> dspList = buildDspList(content, adType);
        logger.info("dspList={}", dspList);
//        根据控制规则过滤dsp，得到可请求的dsp列表(规则对应数据存储在内存中，10分钟更新一次)
        List<String> requestableDsp = buildRequestableDsp(dspList, content);
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
//        在tasks运行期间，获取对应用户的实时点击、曝光数据等实时规则数据, 需要考虑 task.isEmpty
        RealTimeStatus realTimeStatus = buildUserStatus(content, requestableDsp);
        logger.info("realTimeStatus={}", realTimeStatus.toString());
//        获取广告tasks结果集
        for (ITask task : tasks) {
            Optional<List<JSONObject>> apply = task.apply();
            if (apply.isPresent()) {
                List<JSONObject> entries = apply.get();
                if (!entries.isEmpty()) {
                    dataList.addAll(entries);
                } else {
                    logger.error("adEntries.isEmpty");
                }
            } else {
                logger.error("apply is not present!");
            }
        }
//        根据结果集个数填补空余广告位
        if (dataList.size() < content.getRequireNum()) {
            fillAd(dataList, content, requestableDsp, realTimeStatus);
        }

//        处理结果集数据(添加金币，设置空曝光等)，记录结果数据，需要考虑 dataList.isEmpty, realTimeStatus.isEmpty
        List<JSONObject> resultList = resultHandle.handle(dataList, realTimeStatus, content, adType);
        logger.info("resultList={}", resultList);

        return resultList;
    }

    /**
     * 填充广告
     *
     * @param dataList
     * @param content
     * @param requestableDsp
     * @param realTimeStatus
     */
    private void fillAd(List<JSONObject> dataList, RequestContent content, List<String> requestableDsp, RealTimeStatus realTimeStatus) {
        int fillSize = content.getRequireNum() - dataList.size();
        Map<String, DspConfig> dspConfigMap = Variables.getInstance().getDspConfigMap();
        for (int i = 0; i < fillSize; i++) {

        }
    }

    /**
     * 获取用户实时曝光、点击数据
     *
     * @param content
     * @param requestableDsp
     * @return
     */
    private RealTimeStatus buildUserStatus(RequestContent content, List<String> requestableDsp) {
        RealTimeStatus realTimeStatus = new RealTimeStatus();
        realTimeStatus.setAndroidId(content.getAndroidId());
        realTimeStatus.setDeviceId(content.getDeviceId());
        realTimeStatus.setImei(content.getImei());
        for (String dsp : requestableDsp) {
            realTimeStatus.getDspInViewMap().put(dsp, tempCount);
            realTimeStatus.getDspClickMap().put(dsp, tempCount);
            realTimeStatus.getUserClickMap().put(dsp, tempCount);
            realTimeStatus.getUserInViewMap().put(dsp, tempCount);
        }
        tempCount++;
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
     * 构建可以请求的DSP列表
     *
     * @param dspList
     * @param content
     * @return
     */
    private List<String> buildRequestableDsp(List<String> dspList, RequestContent content) {
        List<String> requestableDsp = requestLimit.getRequestableDsp(dspList, content);
        return requestableDsp;
    }

    /**
     * 获取对应版本的可用dsp列表
     *
     * @param content
     * @param adType
     * @return
     */
    private List<String> buildDspList(RequestContent content, AdType adType) {
        List<String> dspList = new LinkedList<>();
        if (AdType.SPLASH.equals(adType)) {
            for (String dsp : Constants.ONLINE_DSP_SPLASH) {
                DspConfig dspConfig = Variables.getInstance().getDspConfigMap().get(dsp);
                if (Constants.PLATFORM_IOS.equalsIgnoreCase(content.getOs())) {
                    if (VersionUtils.compareVersion(content.getVerCode(), dspConfig.getIosMinVersion()) >= 0) {
                        if (!dspConfig.getIosExcludeVersionList().contains(content.getVerCode())) {
                            dspList.add(dsp);
                        }
                    }
                } else {
                    if (VersionUtils.compareVersion(content.getVerCode(), dspConfig.getAndroidMinVersion()) >= 0) {
                        if (!dspConfig.getAndroidExcludeVersionList().contains(content.getVerCode())) {
                            dspList.add(dsp);
                        }
                    }
                }
            }
        } else if (AdType.BANNER.equals(adType)) {

        } else {

        }
        return dspList;
    }
}
