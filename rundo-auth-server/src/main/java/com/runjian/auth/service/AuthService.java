package com.runjian.auth.service;

import com.runjian.auth.vo.response.AuthDataRsp;


/**
 * @author Miracle
 * @date 2023/4/20 10:04
 */
public interface AuthService {

    AuthDataRsp authenticate(String reqUrl, String reqMethod, String jsonStr);

}
