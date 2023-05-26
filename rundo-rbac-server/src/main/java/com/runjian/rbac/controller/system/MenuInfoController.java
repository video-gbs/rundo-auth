package com.runjian.rbac.controller.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.domain.dto.system.HiddenChangeDTO;
import com.runjian.rbac.domain.dto.system.QuerySysMenuInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusChangeDTO;
import com.runjian.rbac.domain.dto.system.SysMenuInfoDTO;
import com.runjian.rbac.domain.vo.system.MenuInfoVO;
import com.runjian.rbac.domain.vo.tree.MenuInfoTree;
import com.runjian.rbac.service.system.MenuInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "菜单管理")
@Slf4j
@RestController
@RequestMapping("/sysMenuInfo")
public class MenuInfoController {

    @Autowired
    private MenuInfoService menuInfoService;


    @PostMapping("/add")
    @ApiOperation("添加菜单")
    public CommonResponse<?> addSysMenu(@RequestBody @Validated({AddGroup.class}) SysMenuInfoDTO dto) {
        log.info("添加菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        menuInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑菜单")
    public CommonResponse<?> updateSysMenu(@RequestBody SysMenuInfoDTO dto) {
        log.info("编辑菜单前端传参信息{}", JSONUtil.toJsonStr(dto));
        menuInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/status/change")
    @ApiOperation("禁用状态切换")
    public CommonResponse<?> statusChange(@RequestBody StatusChangeDTO dto) {
        log.info("菜单禁用状态切换前端传参信息{}", JSONUtil.toJsonStr(dto));
        menuInfoService.modifyByStatus(dto);
        return CommonResponse.success();
    }

    @PostMapping("/hidden/change")
    @ApiOperation("隐藏状态切换")
    public CommonResponse<?> hiddenChange(@RequestBody HiddenChangeDTO dto) {
        log.info("菜单隐藏状态切换前端传参信息{}", JSONUtil.toJsonStr(dto));
        menuInfoService.modifyByHidden(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除菜单")
    public CommonResponse<String> removeSysMenu(@PathVariable Long id) {
        log.info("删除菜单前端传参信息{}", id);
        menuInfoService.erasureById(id);
        return CommonResponse.success();
    }

    @GetMapping("/getById")
    @ApiOperation("通过ID获取菜单详情")
    public CommonResponse<MenuInfoVO> getById(@RequestParam Long id) {
        return CommonResponse.success(menuInfoService.findById(id));
    }

    @PostMapping("/tree")
    @ApiOperation("获取菜单层级树，列表页或者单个应用ID使用")
    public CommonResponse<List<Tree<Long>>> getSysMenuTree(@RequestBody QuerySysMenuInfoDTO dto) {
        return CommonResponse.success(menuInfoService.findByTree(dto));
    }

    @PostMapping("/getTree/{appId}")
    @ApiOperation("根据应用ID获取菜单层级树")
    public CommonResponse<List<Tree<Long>>> getTreeByAppId(@PathVariable Long appId) {
        return CommonResponse.success(menuInfoService.getTreeByAppId(appId));
    }

    @PostMapping("/typeTree/{appType}")
    @ApiOperation("获取同一类应用的菜单层级树")
    public CommonResponse<List<MenuInfoTree>> getMenuTreeByAppType(@PathVariable Integer appType){
        return CommonResponse.success(menuInfoService.findByTreeByAppType(appType));
    }

}
