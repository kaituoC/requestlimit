package com.qknode.bigdata.requestlimit.utils;

import java.text.SimpleDateFormat;

public class TimeUtil {
    private static SimpleDateFormat dayFormater = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat hFormater = new SimpleDateFormat("yyyyMMddHH");
    private static SimpleDateFormat mFormater = new SimpleDateFormat("yyyyMMddHHmm");
    private static SimpleDateFormat m5Formater = new SimpleDateFormat("yyyyMMddHHmm_");
    private static SimpleDateFormat sFormater = new SimpleDateFormat("yyyyMMddHHmmss");

    public static String getD(long ts){
//        synchronized (dayFormater) {
            return dayFormater.format(ts);
//        }
    }

    public static String getD(String date) {
        date = date.replace("-","");
        date = date.replace("+","");
        date = date.replace(":","");
        return date.substring(0,8);
    }

    public static String getH(long ts) {
//        synchronized (hFormater) {
            return hFormater.format(ts);
//        }
    }

    public static String getH(String date) {
        date = date.replace("-","");
        date = date.replace("+","");
        date = date.replace(":","");
        return date.substring(0,10);
    }

    /**
     * 获取当前时间所在的5分钟区间
     * @param ts
     * @return
     */
    private static final int step = 300000;
    public static String getRecentFive(long ts){
//        synchronized (m5Formater) {
            return m5Formater.format(ts + step - ts % step);
//        }
    }

    public static String getRecentFive(String date) {
        date = getM(date);
//        long x = 201702200909L;
        long x = 0;
        try {
            x = Long.parseLong(date);
        } catch (Exception e) {
            return date;
        }
        return x/5 + "";
    }

    public static String getM(long ts){
//        synchronized (mFormater) {
            return mFormater.format(ts);
//        }
    }

    public static String getM(String date) {
        date = date.replace("-","");
        date = date.replace("+","");
        date = date.replace(":","");
        return date.substring(0,12);
    }

    public static String getS(long ts){
        return sFormater.format(ts);
    }

    public static void main(String[] args) {
       System.out.println(new TimeUtil().getH(System.currentTimeMillis()));
    }
}
