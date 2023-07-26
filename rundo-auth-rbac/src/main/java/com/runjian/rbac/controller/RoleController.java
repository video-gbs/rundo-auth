package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.MenuService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.service.rbac.RoleService;
import com.runjian.rbac.vo.request.PostAddRoleReq;
import com.runjian.rbac.vo.request.PostRoleUserAssociateReq;
import com.runjian.rbac.vo.request.PutRoleDisabledReq;
import com.runjian.rbac.vo.request.PutRoleReq;
import com.runjian.rbac.vo.response.*;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * 角色接口
 * @author Miracle
 * @date 2023/6/8 9:42
 */
@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    private final ValidatorService validatorService;

    private final ResourceService resourceService;

    private final MenuService menuService;


    /**
     * 分页查询资源
     * @param resourceKey 资源组
     * @return
     */
    @GetMapping("/resource/tree")
    @ApiDoc(result = GetResourceTreeRsp.class)
    public CommonResponse<GetResourceTreeRsp> getResourcePage(@RequestParam String resourceKey){
        return CommonResponse.success(resourceService.getResourceTree(resourceKey, true));
    }

    /**
     * 获取根节点资源
     * @return
     */
    @GetMapping("/resource/root")
    @ApiDoc(result = GetResourceRootRsp.class)
    public CommonResponse<List<GetResourceRootRsp>> getResourceRoot(){
        return CommonResponse.success(resourceService.getResourceRoot());
    }

    /**
     * 获取菜单树
     * @param name 菜单名称
     * @param path 菜单地址
     * @return 菜单树
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/menu/tree")
    @ApiDoc(result = GetMenuTreeRsp.class)
    public CommonResponse<GetMenuTreeRsp> getMenuTree(){
        return CommonResponse.success(menuService.getMenuList(null, null));
    }

    /**
     * 分页查询角色
     * @param page 页码
     * @param num 数量
     * @param roleName 角色名称
     * @param createBy 创建人
     * @param createTimeStart 创建开始时间
     * @param createTimeEnd 创建结束时间
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page")
    @ApiDoc(result = GetRolePageRsp.class)
    public CommonResponse<PageInfo<GetRolePageRsp>> getRolePage(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int num,
                                                                String roleName, String createBy,
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTimeStart,
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTimeEnd){
        return CommonResponse.success(roleService.getRolePage(page, num, roleName, createBy, createTimeStart, createTimeEnd));
    }

    /**
     * 用户页面的角色查询
     * @param page 页码
     * @param num 数量
     * @param userId 用户id
     * @param roleName 角色名称
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page/user")
    @ApiDoc(result = GetRolePageRsp.class)
    public CommonResponse<PageInfo<GetUserRolePageRsp>> getRolePageByUser(@RequestParam(defaultValue = "1") int page,
                                                                          @RequestParam(defaultValue = "10") int num,
                                                                          Long userId, String roleName){
        return CommonResponse.success(roleService.getRolePage(page, num, userId, roleName));
    }

    /**
     * 获取角色菜单信息
     * @param roleId 角色id
     * @return 菜单id
     */
    @GetMapping("/menu")
    public CommonResponse<Set<Long>> getRoleMenuId(@RequestParam Long roleId){
        return CommonResponse.success(roleService.getRoleMenuIds(roleId));
    }

    /**
     * 获取角色资源信息
     * @param roleId 角色id
     * @return 资源id
     */
    @GetMapping("/resource")
    public CommonResponse<Set<Long>> getRoleResourceId(@RequestParam Long roleId){
        return CommonResponse.success(roleService.getRoleResourceIds(roleId));
    }

    /**
     * 获取角色某菜单下的功能信息
     * @param roleId 角色id
     * @param menuId 菜单id
     * @return 功能id
     */
    @GetMapping("/func/menu")
    public CommonResponse<Set<Long>> getRoleFunc(@RequestParam Long roleId, @RequestParam Long menuId){
        return CommonResponse.success(roleService.getRoleFuncIdByMenuId(roleId, menuId));
    }

    /**
     * 获取角色全部功能信息
     * @param roleId 角色id
     * @return 功能id
     */
    @GetMapping("/func")
    public CommonResponse<Set<Long>> getRoleFunc(@RequestParam Long roleId){
        return CommonResponse.success(roleService.getRoleFuncId(roleId));
    }

    /**
     * 添加角色
     * @param req 添加角色请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addRole(@RequestBody PostAddRoleReq req){
        validatorService.validateRequest(req);
        roleService.addRole(req.getRoleName(), req.getRoleDesc(), req.getMenuIds(), req.getFuncIds(), req.getResourceIds());
        return CommonResponse.success();
    }

    /**
     * 修改角色禁用状态
     * @param req 修改角色禁用状态请求体
     * @return
     */
    @PutMapping("/update/disabled")
    public CommonResponse<?> updateDisabled(@RequestBody PutRoleDisabledReq req){
        validatorService.validateRequest(req);
        roleService.updateDisabled(req.getRoleId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 修改角色
     * @param req 修改角色请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> update(@RequestBody PutRoleReq req){
        validatorService.validateRequest(req);
        roleService.updateRole(req.getRoleId(), req.getRoleName(), req.getRoleDesc(), req.getMenuIds(), req.getFuncIds(), req.getResourceIds());
        return CommonResponse.success();
    }

    /**
     * 批量删除角色
     * @param roleIds 角色id数组
     * @return
     */
    @DeleteMapping("/batch/delete")
    public CommonResponse<?> batchDelete(@RequestParam Set<Long> roleIds){
        roleService.batchDeleteRoles(roleIds);
        return CommonResponse.success();
    }

    /**
     * 角色关联用户
     * @param req 角色关联用户请求体
     * @return
     */
    @PostMapping("/associate")
    public CommonResponse<?> associateUser(@RequestBody PostRoleUserAssociateReq req){
        validatorService.validateRequest(req);
        roleService.associateUser(req.getRoleId(), req.getUserIds(), req.getIsAdd());
        return CommonResponse.success();
    }

}
