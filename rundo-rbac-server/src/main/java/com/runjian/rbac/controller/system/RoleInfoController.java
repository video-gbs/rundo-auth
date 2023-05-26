package com.runjian.rbac.controller.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.constant.UpdateGroup;
import com.runjian.rbac.domain.dto.system.QueryEditUserSysRoleInfoDTO;
import com.runjian.rbac.domain.dto.system.QuerySysRoleInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysRoleInfoDTO;
import com.runjian.rbac.domain.dto.system.SysRoleInfoDTO;
import com.runjian.rbac.domain.vo.system.EditUserSysRoleInfoVO;
import com.runjian.rbac.domain.vo.system.RoleDetailVO;
import com.runjian.rbac.domain.vo.system.SysRoleInfoVO;
import com.runjian.rbac.service.system.RoleInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 角色信息表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "角色管理")
@Slf4j
@RestController
@RequestMapping("/sysRoleInfo")
public class RoleInfoController {


    @Autowired
    private RoleInfoService roleInfoService;

    @PostMapping("/add")
    @ApiOperation("新建角色")
    public CommonResponse<?> addRole(@RequestBody @Validated({AddGroup.class}) SysRoleInfoDTO dto) {
        log.info("新建角色前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/getRoleDetail/{id}")
    @ApiOperation("编辑时回显角色详细信息")
    public CommonResponse<RoleDetailVO> getRoleDetailById(@PathVariable Long id) {
        return CommonResponse.success(roleInfoService.getRoleDetailById(id));
    }


    @PostMapping("/update")
    @ApiOperation("编辑角色")
    public CommonResponse<?> updateRole(@RequestBody @Validated({UpdateGroup.class})  SysRoleInfoDTO dto) {
        log.info("编辑角色,前端传参{}",JSONUtil.toJsonStr(dto));
        roleInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除角色")
    public CommonResponse<?> remove(@PathVariable Long id) {
        roleInfoService.deleteById(id);
        return CommonResponse.success();
    }

    @PostMapping("/batchRemove")
    @ApiOperation("批量删除角色")
    public CommonResponse<?> batchRemove(@RequestBody List<Long> ids) {
        roleInfoService.erasureBatch(ids);
        return CommonResponse.success();
    }

    @PostMapping("/status/change")
    @ApiOperation("接口状态切换")
    public CommonResponse<?> changeStatus(@RequestBody StatusSysRoleInfoDTO dto) {
        log.info("接口状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.modifyByStatus(dto);
        return CommonResponse.success();
    }



    @PostMapping("/getListByPage")
    @ApiOperation("获取角色分页列表")
    public CommonResponse<IPage<SysRoleInfoVO>> getListByPage(@RequestBody QuerySysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.findByPage(dto));
    }

    @PostMapping("/getEditUserSysRoleInfoList")
    @ApiOperation("新增与编辑用户时获取角色分页列表")
    public CommonResponse<IPage<EditUserSysRoleInfoVO>> getEditUserSysRoleInfoList(@RequestBody QueryEditUserSysRoleInfoDTO dto) {
        return CommonResponse.success(roleInfoService.getEditUserSysRoleInfoList(dto));
    }

    @PostMapping("/getAppMenuApiTree/{appType}")
    @ApiOperation("新建角色时获取应用类相关ID列表")
    @ApiImplicitParam(name = "appType", value = "应用分类 1 应用类，2 配置类，3 运维类", required = true)
    public CommonResponse<List<Tree<Long>>> getAppIdTree(@PathVariable Integer appType) {
        return CommonResponse.success(roleInfoService.getAppMenuApiTree(appType));
    }
}
