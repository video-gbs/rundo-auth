package com.runjian.rbac.controller;

import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.MenuService;
import com.runjian.rbac.vo.request.PostMenuReq;
import com.runjian.rbac.vo.request.PutMenuDisabledReq;
import com.runjian.rbac.vo.request.PutMenuHiddenReq;
import com.runjian.rbac.vo.request.PutMenuReq;
import com.runjian.rbac.vo.response.GetMenuTreeRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单接口
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
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page")
    @ApiDoc(result = GetMenuTreeRsp.class)
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

    /**
     * 启禁用菜单
     * @param req 启禁用菜单请求体
     * @return
     */
    @PutMapping("/update/disabled")
    public CommonResponse<?> updateDisabled(@RequestBody PutMenuDisabledReq req){
        validatorService.validateRequest(req);
        menuService.updateDisabled(req.getMenuId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 隐藏菜单
     * @param req 菜单隐藏请求体
     * @return
     */
    @PutMapping("/update/hidden")
    public CommonResponse<?> updateHidden(@RequestBody PutMenuHiddenReq req){
        validatorService.validateRequest(req);
        menuService.updateHidden(req.getMenuId(), req.getHidden());
        return CommonResponse.success();
    }

    /**
     * 修改菜单
     * @param req 修改菜单请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> update(@RequestBody PutMenuReq req){
        validatorService.validateRequest(req);
        menuService.updateMenu(req.getId(), req.getMenuPid(), req.getMenuSort(),
                req.getMenuType(), req.getPath(), req.getComponent(),
                req.getName(), req.getIcon(), req.getDescription(),
                req.getHidden(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 删除
     * @param id 菜单id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> delete(@RequestParam Long id){
        menuService.deleteMenu(id);
        return CommonResponse.success();
    }

}
