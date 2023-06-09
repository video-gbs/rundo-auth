package com.runjian.auth.vo.request;

import lombok.Data;

/**
 * @author Miracle
 * @date 2023/4/19 15:36
 */
@Data
public class PostUserLoginReq {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

}
