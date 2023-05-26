package com.runjian.rbac.controller.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.domain.dto.system.MoveSysOrgDTO;
import com.runjian.rbac.domain.dto.system.SysOrgDTO;
import com.runjian.rbac.domain.vo.system.SysOrgVO;
import com.runjian.rbac.service.system.OrgInfoService;
import com.runjian.common.config.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 组织机构表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "部门管理")
@Slf4j
@RestController
@RequestMapping("/sysOrg")
public class OrgInfoController {

    @Autowired
    private OrgInfoService orgInfoService;

    @PostMapping("/add")
    @ApiOperation("添加部门")
    public CommonResponse<SysOrgVO> save(@RequestBody @Validated({AddGroup.class}) SysOrgDTO dto) {
        log.info("添加部门前端传参信息{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(orgInfoService.save(dto));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除部门")
    public CommonResponse<?> delete(@PathVariable Long id) {
        return orgInfoService.erasureById(id);
    }

    @PostMapping("/update")
    @ApiOperation("编辑部门信息")
    public CommonResponse<?> update(@RequestBody SysOrgDTO dto) {
        log.info("编辑部门信息前端传参信息{}", JSONUtil.toJsonStr(dto));
        orgInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/move")
    @ApiOperation("移动部门")
    public CommonResponse<?> move(@RequestBody @Valid MoveSysOrgDTO dto) {
        orgInfoService.moveSysOrg(dto);
        return CommonResponse.success();
    }

    @GetMapping("/tree")
    @ApiOperation("获取组织机构层级树")
    public CommonResponse<List<Tree<Long>>> getSysOrgById() {
        return CommonResponse.success(orgInfoService.findByTree());
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取部门信息")
    public CommonResponse<SysOrgVO> getById(@PathVariable Long id) {
        return CommonResponse.success(orgInfoService.findById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取部门列表")
    public CommonResponse<List<SysOrgVO>> getList() {
        return CommonResponse.success(orgInfoService.findByList());
    }


}
