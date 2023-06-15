package com.runjian.rbac.utils;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.constant.CommonConstant;
import com.runjian.rbac.vo.dto.AuthDataDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Miracle
 * @date 2023/6/8 15:43
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final HttpServletRequest request;

    /**
     * 获取权限信息
     * @return
     * @throws ClassNotFoundException
     */
    public AuthDataDto getAuthData()  {
        AuthDataDto authDataDto = new AuthDataDto();
        authDataDto.setUsername(request.getHeader("Username"));
        if (Objects.isNull(authDataDto.getUsername())){
            throw new BusinessException(BusinessErrorEnums.USER_FORBID_ACCESS);
        }
        authDataDto.setIsAdmin(Boolean.valueOf(request.getHeader("Is-Admin")));
        authDataDto.setClientId(request.getHeader("Client-Id"));
        if (!authDataDto.getIsAdmin()){
            authDataDto.setRoleIds(Arrays.stream(request.getHeader("Role-Ids").split(",")).map(Long::parseLong).toList());
            authDataDto.setResourceKeyList(Arrays.asList(request.getHeader("Resource-Key").split(",")));
        }
        return authDataDto;
    }

    /**
     * 获取token
     * @return
     */
    public String getAuthToken(){
        return request.getHeader(CommonConstant.AUTHORIZATION);
    }


}
