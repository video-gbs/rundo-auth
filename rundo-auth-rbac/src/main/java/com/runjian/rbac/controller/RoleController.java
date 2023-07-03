package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.RoleService;
import com.runjian.rbac.vo.request.PostAddRoleReq;
import com.runjian.rbac.vo.request.PostRoleUserAssociateReq;
import com.runjian.rbac.vo.request.PutRoleDisabledReq;
import com.runjian.rbac.vo.request.PutRoleReq;
import com.runjian.rbac.vo.response.GetRolePageRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                                                                LocalDateTime createTimeStart, LocalDateTime createTimeEnd){
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
    public CommonResponse<PageInfo<GetRolePageRsp>> getRolePageByUser(@RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "10") int num,
                                                                      Long userId, String roleName){
        return CommonResponse.success(roleService.getRolePage(page, num, userId, roleName));
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
    @DeleteMapping("/delete/batch")
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
        roleService.associateUser(req.getRoleId(), req.getUserIds());
        return CommonResponse.success();
    }

}
