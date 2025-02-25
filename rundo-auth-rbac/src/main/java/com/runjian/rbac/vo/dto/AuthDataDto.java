package com.runjian.rbac.vo.dto;

import com.runjian.rbac.constant.AuthStringEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 授权数据
 * @author Miracle
 * @date 2023/4/24 11:29
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDataDto {

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
    private List<String> resourceKeyList = new ArrayList<>();




}
