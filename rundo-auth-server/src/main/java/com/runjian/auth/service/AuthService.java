package com.runjian.auth.service;

import com.runjian.auth.vo.response.AuthDataRsp;


/**
 * @author Miracle
 * @date 2023/4/20 10:04
 */
public interface AuthService {

    /**
     * 鉴权
     * @param reqUrl 访问url
     * @param reqMethod 访问方式
     * @param jsonStr 数据体
     * @return
     */
    AuthDataRsp authenticate(String reqUrl, String reqMethod, String jsonStr);

    /**
     * 登出
     */
    void logout(String token);

    /**
     * 注销用户
     * @param username 用户名
     */
    void signOut(String username);

}
