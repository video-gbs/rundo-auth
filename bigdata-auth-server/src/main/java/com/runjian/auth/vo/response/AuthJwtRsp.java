package com.runjian.auth.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/14 15:23
 */
@Data
public class AuthJwtRsp {

    /**
     * jwt是否可用
     */
    private Boolean active = false;

    /**
     * 用户名
     */
    @JsonProperty("sub")
    private String username;

    /**
     * 客户端数组
     */
    @JsonProperty("aud")
    private List<String> clients;

    /**
     * 范围
     */
    private String scope;

    /**
     * token类型
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * 客户端id
     */
    @JsonProperty("client_id")
    private String clientId;
}
