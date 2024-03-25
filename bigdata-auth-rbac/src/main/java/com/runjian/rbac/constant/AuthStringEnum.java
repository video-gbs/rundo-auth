package com.runjian.rbac.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/8/2 10:12
 */
@Getter
@AllArgsConstructor
public enum AuthStringEnum {

    USER_NO_ROLE("当前用户没有任何角色的权限"),
    USER_NO_FUNC("当前用户没有功能'%s'的权限"),
    USER_NO_FUNC_PARAM("必要的参数权限校验失败，缺失参数'%s'"),
    USER_NO_FUNC_RESOURCE("当前用户没有该资源的操作权限"),
    FUNC_IS_DISABLED("功能'%s'已被禁用"),
    CLIENT_NO_FUNC("当前用户没有功能'%s'的权限")
    ;

    private final String msg;

    public String getFormat(Object param){
        if (Objects.isNull(param)){
            return this.msg;
        }
        return String.format(this.msg, param);
    }
}
