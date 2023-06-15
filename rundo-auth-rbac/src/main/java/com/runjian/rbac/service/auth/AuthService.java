package com.runjian.rbac.service.auth;

import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.dto.AuthUserDto;

/**
 * @author Miracle
 * @date 2023/6/6 16:02
 */
public interface AuthService {

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
     * @param jsonStr 数据体
     * @return AuthDataDto
     */
    AuthDataDto getAuthDataByUser(String username, String scope, String reqPath, String reqMethod, String jsonStr);

    /**
     * 获取客户端授权信息
     * @param scope 授权体系
     * @param reqPath 访问路径
     * @param reqMethod 访问方式
     * @return AuthDataDto
     */
    AuthDataDto getAuthDataByClient(String scope, String reqPath, String reqMethod);

    /**
     * 登出
     */
    void logout();
}
