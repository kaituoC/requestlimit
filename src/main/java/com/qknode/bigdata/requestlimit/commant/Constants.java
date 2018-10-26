package com.qknode.bigdata.requestlimit.commant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author kaituo
 * @date 2018-10-18
 */
public class Constants {
    public static final int DAILY_USER_CLICK_MIN_LIMIT = 2;
    public static final int DAILY_USER_CLICK_MAX_LIMIT = 5;
    public static final String A = "a";
    public static final String B = "b";
    public static final String C = "c";
    public static final List<String> ONLINE_DSP = new LinkedList<String>(){{
        add(A);
        add(B);
        add(C);
    }};
    public static final Map<String, Long> REQUEST_LIMIT = new HashMap<String, Long>(){{
        put(A, 5L);
        put(B, 10L);
        put(C, 15L);
    }};
    public static final String DSP_REQUEST_PREFIX = "DspRequest:";
}
