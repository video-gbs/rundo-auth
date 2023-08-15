package com.runjian.auth.constant;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * @author Miracle
 * @date 2023/8/15 10:05
 */
@Getter
@AllArgsConstructor
public enum AuthGrantType {

    AUTHORIZATION_CODE(1, "authorization_code", AuthorizationGrantType.AUTHORIZATION_CODE),
    REFRESH_TOKEN(2, "refresh_token", AuthorizationGrantType.REFRESH_TOKEN),
    CLIENT_CREDENTIALS(3, "client_credentials", AuthorizationGrantType.CLIENT_CREDENTIALS),
    PASSWORD(4, "password", AuthorizationGrantType.PASSWORD),
    JWT_BEARER(5, "urn:ietf:params:oauth:grant-type:jwt-bearer", AuthorizationGrantType.JWT_BEARER)
    ;

    private final Integer code;

    private final String msg;

    private final AuthorizationGrantType authorizationGrantType;

    public static AuthGrantType getByMsg(String msg){
        for (AuthGrantType authGrantType : AuthGrantType.values()) {
            if (authGrantType.getMsg().equals(msg)){
                return authGrantType;
            }
        }
        throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "不存在的授权方式");
    }

    public static AuthGrantType getByCode(Integer code){
        for (AuthGrantType authGrantType : AuthGrantType.values()) {
            if (authGrantType.getCode().equals(code)){
                return authGrantType;
            }
        }
        throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "不存在的授权方式");
    }

    public static boolean inAuthGrantTypeScope(Integer code){
        for (AuthGrantType authGrantType : AuthGrantType.values()) {
            if (authGrantType.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }

}
