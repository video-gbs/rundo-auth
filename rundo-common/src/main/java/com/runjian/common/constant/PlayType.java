package com.runjian.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/2/6 11:49
 */
@Getter
@AllArgsConstructor
public enum PlayType {

    LIVE(1, "LIVE"),
    RECORD(2, "RECORD"),
    ALARM(3, "ALARM"),
    DOWNLOAD(4, "DOWNLOAD");

    private Integer code;
    private String msg;

    public static String getMsgByCode(int code){
        if (code == LIVE.code){
            return LIVE.msg;
        } else if (code == RECORD.code) {
            return RECORD.msg;
        } else if (code == DOWNLOAD.code) {
            return DOWNLOAD.msg;
        } else {
            return null;
        }
    }
}
