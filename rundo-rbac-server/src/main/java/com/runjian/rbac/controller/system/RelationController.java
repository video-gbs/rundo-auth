package com.runjian.rbac.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.rbac.domain.dto.system.QueryRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.QueryRoleRelationSysUserInfoDTO;
import com.runjian.rbac.domain.dto.system.RoleRelationUserDTO;
import com.runjian.rbac.domain.vo.system.RelationSysUserInfoVO;
import com.runjian.rbac.service.system.RoleInfoService;
import com.runjian.rbac.service.system.UserInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RelationController
 * @Description 关联用户相关
 * @date 2023-03-13 周一 9:19
 */
@Api(tags = "关联用户管理")
@Slf4j
@RestController
@RequestMapping("/")
public class RelationController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RoleInfoService roleInfoService;

    @PostMapping("sysUserInfo/getRelationSysUserInfo/{id}")
    @ApiOperation("关联用户查看单个用户信息")
    public CommonResponse<RelationSysUserInfoVO> getRelationSysUserInfoById(@PathVariable Long id) {
        return CommonResponse.success(userInfoService.findRelationById(id));
    }

    @PostMapping("sysUserInfo/getRelationSysUserInfoList")
    @ApiOperation("关联用户用户列表")
    public CommonResponse<IPage<RelationSysUserInfoVO>> getRelationSysUserInfoList(@RequestBody QueryRelationSysUserInfoDTO dto) {
        log.info("关联用户用户列表前端传参{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(userInfoService.findRelationList(dto));
    }

    @PostMapping("sysRoleInfo/relationUser/add")
    @ApiOperation("提交关联用户列表")
    public CommonResponse<?> addRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("提交关联用户列表前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.addRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("sysRoleInfo/relationUser/right")
    @ApiOperation("右移从关联用户列表中移除用户")
    public CommonResponse<?> rightRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("右移从关联用户列表中移除用户，前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.rightRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("sysRoleInfo/relationUser/left")
    @ApiOperation("左移提交用户到已关联用户列表")
    public CommonResponse<?> leftRelationUser(@RequestBody RoleRelationUserDTO dto) {
        log.info("左移提交用户到已关联用户列表，前端传参{}", JSONUtil.toJsonStr(dto));
        roleInfoService.leftRelationUser(dto);
        return CommonResponse.success();
    }

    @PostMapping("sysRoleInfo/relationUserByRole")
    @ApiOperation("查询已关联用户列表")
    public CommonResponse<IPage<RelationSysUserInfoVO>> listRelationUser(@RequestBody @Valid QueryRoleRelationSysUserInfoDTO dto) {
        log.info("查询已关联用户列表传参{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(roleInfoService.listRelationUser(dto));
    }
}
