package com.runjian.auth.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/4/24 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataRsp {

    /**
     * 状态码
     */
    private Integer statusCode;

    /**
     * 是否是超管
     */
    private Boolean isAdmin;

    /**
     * 用户名
     */
    private String username;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 角色id数组
     */
    private List<Long> roleIds;

    /**
     * 消息
     */
    private String msg;

    /**
     * 授权的资源
     */
    private String resourceKey;



    /**
     * 获取失败返回
     * @param msg 消息
     * @return
     */
    public static AuthDataRsp getFailureRsp(String msg, Integer statusCode){
        AuthDataRsp authDataRsp = new AuthDataRsp();
        authDataRsp.setMsg(msg);
        authDataRsp.setStatusCode(statusCode);
        return authDataRsp;
    }

}
