package com.runjian.auth.service;

import com.runjian.auth.vo.response.AuthDataRsp;


/**
 * @author Miracle
 * @date 2023/4/20 10:04
 */
public interface AuthService {

    /**
     * 初始化
     */
    void init();

    /**
     * 定期清理过期时间
     */
    void clearOutTimeToken();

    /**
     * 鉴权
     * @param reqUrl 访问url
     * @param reqMethod 访问方式
     * @param queryData  查询数据体
     * @param bodyData 数据体
     * @return
     */
    AuthDataRsp authenticate(String reqUrl, String reqMethod, String queryData, String bodyData);

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
