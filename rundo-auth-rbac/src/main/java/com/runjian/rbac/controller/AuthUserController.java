package com.runjian.rbac.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.rbac.service.auth.AuthUserService;
import com.runjian.rbac.vo.response.GetFuncRsp;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import com.runjian.rbac.vo.response.GetUserRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 获取权限数据接口
 * @author Miracle
 * @date 2023/6/15 17:43
 */
@Slf4j
@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class AuthUserController {

    private final AuthUserService authUserService;

    /**
     * 登出系统
     * @return
     */
    @DeleteMapping("/logout")
    public CommonResponse<?> logout(){
        authUserService.logout();
        return CommonResponse.success();
    }

    /**
     * 获取用户信息
     * @return GetUserRsp
     */
    @GetMapping("/user")
    @ApiDoc(result = GetUserRsp.class)
    public CommonResponse<GetUserRsp> getUser(){
        return CommonResponse.success(authUserService.getUser());
    }

    /**
     * 获取菜单数据
     * @param levelNumStart 层级数开始
     * @param levelNumEnd 层级数结束，可以为空，查询全部
     * @return
     */
    @GetMapping("/menu")
    @ApiDoc(result = GetMenuTreeRsp.class)
    public CommonResponse<List<GetMenuTreeRsp>> getMenu(@RequestParam Integer levelNumStart, Integer levelNumEnd){
        return CommonResponse.success(authUserService.getMenu(levelNumStart, levelNumEnd));
    }

    /**
     * 获取功能数据
     * @param menuId 菜单id
     * @return
     */
    @GetMapping("/func")
    @ApiDoc(result = GetFuncRsp.class)
    public CommonResponse<List<GetFuncRsp>> getFunc(@RequestParam Integer menuId){
        return CommonResponse.success(authUserService.getFunc(menuId));
    }

    /**
     * 获取资源数据
     * @param resourceKey 资源key
     * @param isIncludeResource 是否包含资源
     * @return
     */
    @GetMapping("/resource")
    @ApiDoc(result = GetResourceTreeRsp.class)
    public CommonResponse<GetResourceTreeRsp> getResource(@RequestParam String resourceKey, @RequestParam Boolean isIncludeResource){
        return CommonResponse.success(authUserService.getResource(resourceKey, isIncludeResource));
    }
}
