package com.runjian.rbac.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.domain.dto.system.QuerySysAppInfoDTO;
import com.runjian.rbac.domain.dto.system.StatusSysAppInfoDTO;
import com.runjian.rbac.domain.dto.system.SysAppInfoDTO;
import com.runjian.rbac.domain.vo.system.SysAppInfoVO;
import com.runjian.rbac.service.system.AppInfoService;
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
 * 应用信息 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "应用管理")
@Slf4j
@RestController
@RequestMapping("/sysAppInfo")
public class AppInfoController {
    @Autowired
    private AppInfoService appInfoService;

    @PostMapping("/add")
    @ApiOperation("添加应用")
    public CommonResponse<?> save(@RequestBody @Validated({AddGroup.class}) SysAppInfoDTO dto) {
        log.info("添加应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        appInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("编辑应用")
    public CommonResponse<?> modify(@RequestBody SysAppInfoDTO dto) {
        log.info("编辑应用信息前端传参{}", JSONUtil.toJsonStr(dto));
        appInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除应用")
    public CommonResponse<?> remove(@PathVariable Long id) {
        log.info("删除应用信息前端传参{}", id);
        appInfoService.erasureById(id);
        return CommonResponse.success();
    }

    @PostMapping("/status/change")
    @ApiOperation("应用状态切换")
    public CommonResponse<?> changeStatus(@RequestBody StatusSysAppInfoDTO dto) {
        log.info("应用状态切换前端传参{}", JSONUtil.toJsonStr(dto));
        appInfoService.modifyByStatus(dto);
        return CommonResponse.success();
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取应用信息")
    public CommonResponse<SysAppInfoVO> getById(@PathVariable Long id) {
        return CommonResponse.success(appInfoService.findById(id));
    }

    @GetMapping("/getList")
    @ApiOperation("获取应用列表 无分页")
    public CommonResponse<List<SysAppInfoVO>> getList() {
        return CommonResponse.success(appInfoService.findByList());
    }

    @PostMapping("/getListByPage")
    @ApiOperation("获取应用列表 分页")
    public CommonResponse<IPage<SysAppInfoVO>> getListByPage(@RequestBody QuerySysAppInfoDTO dto) {
        return CommonResponse.success(appInfoService.findByPage(dto));
    }


}
