package com.runjian.auth.controller;


import com.github.pagehelper.PageInfo;
import com.runjian.auth.service.AuthClientService;
import com.runjian.auth.vo.request.PostAuthClientReq;
import com.runjian.auth.vo.request.PutAuthClientReq;
import com.runjian.auth.vo.response.GetAuthClientPage;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author Miracle
 * @date 2023/8/15 15:12
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth-client")
public class AuthClientController {

    private final AuthClientService authClientService;

    private final ValidatorService validatorService;

    /**
     * 分页查询授权客户端
     * @param page 页码
     * @param num 每页行数
     * @param clientId 客户端账号
     * @param clientName 客户端名称
     * @return
     */
    @GetMapping("/page")
    @BlankStringValid
    @IllegalStringValid
    public CommonResponse<PageInfo<GetAuthClientPage>> getAuthClientPage(@RequestParam(defaultValue = "1") int page,
                                                                         @RequestParam(defaultValue = "10") int num,
                                                                         String clientId, String clientName){
        return CommonResponse.success(authClientService.getClientPage(page, num, clientId, clientName));
    }

    /**
     * 添加授权客户端
     * @param req 添加授权客户端请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addAuthClient(@RequestBody PostAuthClientReq req){
        validatorService.validateRequest(req);
        authClientService.addClient(req.getClientId(), req.getClientSecret(), req.getClientName(),
                req.getClientAuthenticationMethods(), req.getAuthorizationGrantTypes(), req.getScopes(),
                req.getRedirectUris(), req.getRequireAuthorizationConsent(), req.getAccessTokenTimeToLiveSecond(),
                req.getRefreshTokenTimeToLiveSecond(), req.getAuthCodeTimeToLiveSecond(), req.getClientSecretExpiresAt());
        return CommonResponse.success();
    }

    /**
     * 修改授权客户端
     * @param req 修改授权客户端请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateAuthClient(@RequestBody PutAuthClientReq req){
        validatorService.validateRequest(req);
        authClientService.updateClient(req.getId(), req.getClientSecret(), req.getClientName(),
                req.getClientAuthenticationMethods(), req.getAuthorizationGrantTypes(), req.getScopes(),
                req.getRedirectUris(), req.getRequireAuthorizationConsent(), req.getAccessTokenTimeToLiveSecond(),
                req.getRefreshTokenTimeToLiveSecond(), req.getAuthCodeTimeToLiveSecond(), req.getClientSecretExpiresAt());
        return CommonResponse.success();
    }

    /**
     * 删除客户端
     * @param id 主键id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteAuthClient(@RequestParam String id){
        authClientService.deleteClient(id);
        return CommonResponse.success();
    }
}
