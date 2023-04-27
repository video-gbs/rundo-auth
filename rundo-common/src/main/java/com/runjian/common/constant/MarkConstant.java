package com.runjian.common.constant;

/**
 * @author Miracle
 * @date 2022/4/7 16:53
 */
public class MarkConstant {

    /*********************************设备类型标志*******************************************/

    public static final String REDIS_DEVICE_ONLINE_STATE = "DEVICE_ONLINE_STATE";

    public static final String REDIS_DEVICE_ONLINE_STATE_LOCK = "DEVICE_ONLINE_STATE_LOCK";


    public static final String REDIS_CHANNEL_ONLINE_STATE = "CHANNEL_ONLINE_STATE";

    public static final String REDIS_CHANNEL_ONLINE_STATE_LOCK = "REDIS_CHANNEL_ONLINE_STATE_LOCK";

    /*********************************标志位分割符号*******************************************/

    public static final String MARK_SPLIT_SYMBOL = "_";

    public static final String MARK_SPLIT_SEMICOLON = ":";

    public static final String MARK_SPLIT_RAIL = "-";

    /*********************************未完成任务定时重试标志*******************************************/



    /*********************************视频类型标志*******************************************/


    /*******************************录像计划定时器标志****************************************/



    /*******************************用户相关****************************************/

    /*******************************feign错误解析****************************************/

    public static final String MARK_FEIGN_RESP_START = "[{";
    public static final String MARK_FEIGN_RESP_END = "}]";
    public static final String MARK_FEIGN_RESP_CODE = "code";




}
