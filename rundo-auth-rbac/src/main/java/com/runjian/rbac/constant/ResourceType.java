package com.runjian.rbac.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Miracle
 * @date 2023/6/12 10:23
 */
@Getter
@AllArgsConstructor
public enum ResourceType {

    CATALOGUE(1),
    RESOURCE(2);


    private final Integer code;
}
