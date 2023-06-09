package com.runjian.auth.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Miracle
 * @date 2023/4/24 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizeData {

    /**
     * 是否授权
     */
    private Boolean isAuthorized;

    /**
     * 用户名
     */
    private String username;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 授权范围
     */
    private String scopes;

    /**
     * 授权的资源
     */
    private Object authResource;


}
