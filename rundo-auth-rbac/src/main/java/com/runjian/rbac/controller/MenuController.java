package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.MenuService;
import com.runjian.rbac.vo.request.PostMenuReq;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import com.runjian.rbac.vo.response.GetResourcePageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Miracle
 * @date 2023/6/8 9:43
 */
@Slf4j
@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    private final ValidatorService validatorService;

    /**
     * 获取菜单树
     * @param name 菜单名称
     * @param path 菜单地址
     * @return 菜单树
     */
    @GetMapping("/page")
    public CommonResponse<List<GetMenuTreeRsp>> getMenuTree(String name, String path){
        menuService.getMenuList(name, path);
        return CommonResponse.success(menuService.getMenuList(name, path));
    }

    /**
     * 添加菜单
     * @param req 添加菜单请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addMenu(@RequestBody PostMenuReq req){
        validatorService.validateRequest(req);
        menuService.addMenu(req.getMenuPid(), req.getMenuSort(), req.getMenuType(),
                req.getPath(), req.getComponent(), req.getName(),
                req.getIcon(), req.getDescription(), req.getHidden(),
                req.getDisabled());
        return CommonResponse.success();
    }
}
