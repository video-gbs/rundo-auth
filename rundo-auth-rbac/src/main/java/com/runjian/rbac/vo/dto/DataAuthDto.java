package com.runjian.rbac.vo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Miracle
 * @date 2023/4/24 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataAuthDto {

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
     * 消息
     */
    private String msg;

    /**
     * 授权的资源
     */
    private Map<String, Object> resourceMap = new HashMap<>();


    public void put(String key, Object value){
        this.resourceMap.put(key, value);
    }


}
