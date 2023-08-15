package com.runjian.auth.constant;

import com.runjian.common.config.exception.BusinessErrorEnums;
import com.runjian.common.config.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

/**
 * @author Miracle
 * @date 2023/8/15 11:30
 */
@Getter
@AllArgsConstructor
public enum ClientAuthMethod {

    CLIENT_SECRET_BASIC(1, "client_secret_basic", ClientAuthenticationMethod.CLIENT_SECRET_BASIC),
    CLIENT_SECRET_POST(2, "client_secret_post", ClientAuthenticationMethod.CLIENT_SECRET_POST),
    CLIENT_SECRET_JWT(3, "client_secret_jwt", ClientAuthenticationMethod.CLIENT_SECRET_JWT),
    PRIVATE_KEY_JWT(4, "private_key_jwt", ClientAuthenticationMethod.PRIVATE_KEY_JWT),
    NONE(5, "none", ClientAuthenticationMethod.NONE)
    ;

    private final Integer code;

    private final String msg;

    private final ClientAuthenticationMethod clientAuthenticationMethod;

    public static ClientAuthMethod getByMsg(String msg){
        for (ClientAuthMethod clientAuthMethod : ClientAuthMethod.values()) {
            if (clientAuthMethod.getMsg().equals(msg)){
                return clientAuthMethod;
            }
        }
        throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "不存在的客户端授权方式");
    }

    public static ClientAuthMethod getByCode(Integer code){
        for (ClientAuthMethod clientAuthMethod : ClientAuthMethod.values()) {
            if (clientAuthMethod.getCode().equals(code)){
                return clientAuthMethod;
            }
        }
        throw new BusinessException(BusinessErrorEnums.VALID_BIND_EXCEPTION_ERROR, "不存在的客户端授权方式");
    }

    public static boolean inClientAuthMethodScope(Integer code){
        for (ClientAuthMethod clientAuthMethod : ClientAuthMethod.values()) {
            if (clientAuthMethod.getCode().equals(code)){
                return true;
            }
        }
        return false;
    }


}
