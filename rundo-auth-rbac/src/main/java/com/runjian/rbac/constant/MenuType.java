package com.runjian.rbac.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/6/5 16:00
 */
@Getter
@AllArgsConstructor
public enum MenuType {

    DIRECTORY(1, "目录"),
    PAGE(2, "页面")
    ;


    private final Integer code;

    private final String msg;
}
