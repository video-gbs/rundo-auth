package com.runjian.rbac.constant;

import com.runjian.common.constant.MsgType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/6/16 15:29
 */
@Getter
@AllArgsConstructor
public enum MethodType {

    GET(1, "GET"),
    POST(2, "POST"),
    PUT(3, "PUT"),
    DELETE(4, "DELETE")

    ;

    private final Integer code;

    private final String msg;

    public static MethodType getByCode(int code) {
        for (MethodType methodType : MethodType.values()) {
            if (methodType.getCode().equals(code)){
                return methodType;
            }
        }
        return MethodType.GET;
    }
}
