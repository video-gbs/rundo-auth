package com.runjian.rbac.service.auth;

import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.AuthUserDto;

/**
 * @author Miracle
 * @date 2023/6/6 16:02
 */
public interface AuthSystemService {

    /**
     * 获取授权用户
     * @param username 用户名
     * @return
     */
    AuthUserDto getUserAuth(String username);

    /**
     * 获取用户授权信息
     * @param username 用户名
     * @param scope 授权体系
     * @param reqPath 访问路径
     * @param reqMethod 访问方式
     * @param queryData query数据
     * @param bodyData body数据
     * @return AuthDataDto
     */
    AuthDataDto getAuthDataByUser(String username, String scope, String reqPath, String reqMethod, String queryData, String bodyData);

    /**
     * 获取客户端授权信息
     * @param scope 授权体系
     * @param reqPath 访问路径
     * @param reqMethod 访问方式
     * @return AuthDataDto
     */
    AuthDataDto getAuthDataByClient(String scope, String reqPath, String reqMethod);


}
