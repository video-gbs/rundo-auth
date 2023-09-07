package com.runjian.auth.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.auth.constant.AuthGrantType;
import com.runjian.auth.constant.ClientAuthMethod;
import com.runjian.auth.utils.ValidUrlUtils;
import com.runjian.common.config.exception.BusinessException;
import com.runjian.common.validator.ValidationResult;
import com.runjian.common.validator.ValidatorFunction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 添加授权客户端请求体
 * @author Miracle
 * @date 2023/8/15 15:19
 */
@Data
public class PostAuthClientReq implements ValidatorFunction {

    /**
     * 客户端账号
     */
    @NotBlank(message = "客户端账号不能为空")
    @Size(min = 1, max = 100, message = "客户端账号的范围在1~100")
    private String clientId;

    /**
     * 客户端密码
     */
    @NotBlank(message = "客户端密码不能为空")
    @Size(min = 6, max = 999, message = "客户端密码的范围在1~999")
    private String clientSecret;

    /**
     * 客户端名称
     */
    @NotBlank(message = "客户端名称不能为空")
    @Size(min = 1, max = 100, message = "客户端名称的范围在1~100")
    private String clientName;

    /**
     * 客户端授权方式
     */
    @NotNull(message = "客户端授权方式不能为空")
    @Size(min = 1, max = 5, message = "客户端授权方式最多5个")
    private Set<Integer> clientAuthenticationMethods;

    /**
     * 用户授权登录方式
     */
    @NotNull(message = "用户授权方式不能为空")
    @Size(min = 1, max = 5, message = "用户授权方式最多5个")
    private Set<Integer> authorizationGrantTypes;

    /**
     * 授权范围
     */
    @NotNull(message = "授权范围不能为空")
    @Size(min = 1, max = 999, message = "授权范围过多")
    private Set<String> scopes;

    /**
     * 授权跳转url
     */
    @Size(max = 999, message = "授权跳转url过多")
    private Set<String> redirectUris;

    /**
     * 是否需要用户授权
     */
    @NotNull(message = "是否需要用户授权不能为空")
    private Boolean requireAuthorizationConsent;

    /**
     * 授权token持续时间，单位：秒
     */
    @Range(min = 300, max = 999999999, message = "授权token持续时间最低为300秒")
    private Long accessTokenTimeToLiveSecond;

    /**
     * 刷新token持续时间，单位：秒
     */
    @Range(min = 300, max = 999999999, message = "非法授权token持续时间最低为300秒")
    private Long refreshTokenTimeToLiveSecond;

    /**
     * 授权code持续时间
     */
    @Range(min = 15, max = 999999999, message = "非法授权token持续时间最低为15秒")
    private Long authCodeTimeToLiveSecond;

    /**
     * 密码过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime clientSecretExpiresAt;

    @Override
    public void validEvent(ValidationResult result, Object data, Object matchData) throws BusinessException {
        if (Objects.nonNull(this.accessTokenTimeToLiveSecond) &&  Objects.nonNull(this.refreshTokenTimeToLiveSecond) && (this.accessTokenTimeToLiveSecond > this.refreshTokenTimeToLiveSecond)){
            result.setHasErrors(true);
            result.getErrorMsgMap().put("客户端授权方式请求参数有误", "刷新token持续时间必须大于授权token持续时间");
        }else if (Objects.isNull(this.accessTokenTimeToLiveSecond) && Objects.nonNull(this.refreshTokenTimeToLiveSecond) && this.refreshTokenTimeToLiveSecond < 3600){
            result.setHasErrors(true);
            result.getErrorMsgMap().put("客户端授权方式请求参数有误", "授权token未填，默认为3600秒，刷新token持续时间必须大于授权token持续时间");
        } else if (Objects.isNull(this.refreshTokenTimeToLiveSecond) && Objects.nonNull(this.accessTokenTimeToLiveSecond) && this.accessTokenTimeToLiveSecond > 10800) {
            result.setHasErrors(true);
            result.getErrorMsgMap().put("客户端授权方式请求参数有误", "刷新token未填，默认为10800秒，刷新token持续时间必须大于授权token持续时间");
        }

        for (Integer clientAuthMethod : clientAuthenticationMethods){
            if (!ClientAuthMethod.inClientAuthMethodScope(clientAuthMethod)){
                result.setHasErrors(true);
                result.getErrorMsgMap().put("客户端授权方式请求参数有误", String.format("参数'%s'非合法的客户端授权方式", clientAuthMethod));
            }
        }
        for (Integer authGrantType : authorizationGrantTypes){
            if (!AuthGrantType.inAuthGrantTypeScope(authGrantType)){
                result.setHasErrors(true);
                result.getErrorMsgMap().put("用户授权方式请求参数有误", String.format("参数'%s'非合法的用户授权方式", authGrantType));
            }
        }
        for (String url : this.redirectUris){
            if(!ValidUrlUtils.validateUri(url)){
                result.setHasErrors(true);
                result.getErrorMsgMap().put("回调地址请求参数有误", String.format("参数'%s'非合法的回调地址", url));
            }
        }
    }
}
