package com.runjian.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型
 * @author Miracle
 * @date 2023/1/12 9:43
 */

@Getter
@AllArgsConstructor
public enum MsgType {

    /****************** 网关相关消息 ******************/
    GATEWAY_SIGN_IN("GATEWAY_SIGN_IN"),
    GATEWAY_RE_SIGN_IN("GATEWAY_RE_SIGN_IN"),
    GATEWAY_HEARTBEAT("GATEWAY_HEARTBEAT"),


    /****************** 调度相关消息 ******************/

    DISPATCH_SIGN_IN("DISPATCH_SIGN_IN"),

    DISPATCH_RE_SIGN_IN("DISPATCH_RE_SIGN_IN"),

    DISPATCH_HEARTBEAT("DISPATCH_HEARTBEAT"),

    /****************** 设备相关消息 ******************/

    DEVICE_TOTAL_SYNC("DEVICE_TOTAL_SYNC"),
    DEVICE_SIGN_IN("DEVICE_SIGN_IN"),
    DEVICE_SYNC("DEVICE_SYNC"),
    DEVICE_DELETE("DEVICE_DELETE"),
    DEVICE_ADD("DEVICE_ADD"),

    /****************** 通道相关消息 ******************/
    CHANNEL_SYNC("CHANNEL_SYNC"),
    CHANNEL_PLAY("CHANNEL_PLAY"),
    CHANNEL_PLAYBACK("CHANNEL_PLAYBACK"),
    CHANNEL_STOP_PLAY("CHANNEL_STOP_PLAY"),
    CHANNEL_PTZ_CONTROL("CHANNEL_PTZ_CONTROL"),
    CHANNEL_PTZ_PRESET("CHANNEL_PTZ_PRESET"),
    CHANNEL_PTZ_3D("CHANNEL_PTZ_3D"),
    CHANNEL_RECORD_INFO("CHANNEL_RECORD_INFO"),

    /****************** 流媒体管理服务相关消息 ******************/
    STREAM_PLAY_RESULT("STREAM_PLAY_RESULT"),
    STREAM_CLOSE("STREAM_CLOSE"),
    STREAM_PLAY_STOP("STREAM_PLAY_STOP"),
    STREAM_RECORD_START("STREAM_RECORD_START"),
    STREAM_RECORD_STOP("STREAM_RECORD_STOP"),
    STREAM_RECORD_SPEED("STREAM_RECORD_SPEED"),
    STREAM_RECORD_SEEK("STREAM_RECORD_SEEK"),
    STREAM_RECORD_PAUSE("STREAM_RECORD_PAUSE"),
    STREAM_RECORD_RESUME("STREAM_RECORD_RESUME"),
    STREAM_CHECK_RECORD("STREAM_CHECK_RECORD"),
    STREAM_CHECK_STREAM("STREAM_CHECK_STREAM"),
    STREAM_STOP_ALL("STREAM_STOP_ALL"),
    STREAM_MEDIA_INFO("STREAM_MEDIA_INFO"),

    ;

    private final String msg;

    public static MsgType getByStr(String msgStr) {
        if (msgStr == null) {
            return null;
        }
        for (MsgType msgType : MsgType.values()) {
            if (msgStr.equals(msgType.getMsg())){
                return msgType;
            }
        }
        return null;
    }



}
