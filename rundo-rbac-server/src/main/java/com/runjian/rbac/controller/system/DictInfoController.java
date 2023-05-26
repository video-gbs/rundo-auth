package com.runjian.rbac.controller.system;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.domain.dto.system.QureySysDictDTO;
import com.runjian.rbac.domain.dto.system.SysDictDTO;
import com.runjian.rbac.domain.vo.system.SysDictVO;
import com.runjian.rbac.service.system.DictInfoService;
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
 * 数据字典表 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "数据字典")
@Slf4j
@RestController
@RequestMapping("/sysDict")
public class DictInfoController {

    @Autowired
    private DictInfoService dictInfoService;

    @PostMapping("/add")
    @ApiOperation("添加数据字典")
    public CommonResponse<?> save(@RequestBody @Validated({AddGroup.class}) SysDictDTO dto) {
        log.info("添加数据字典前端传参{}", JSONUtil.toJsonStr(dto));
        dictInfoService.save(dto);
        return CommonResponse.success();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除数据字典")
    public CommonResponse<?> remove(@PathVariable Long id) {
        log.info("删除数据字典前端传参{}", id);
        dictInfoService.erasureById(id);
        return CommonResponse.success();
    }

    @PostMapping("/update")
    @ApiOperation("修改数据字典")
    public CommonResponse<?> updateSysDict(@RequestBody SysDictDTO dto) {
        dictInfoService.modifyById(dto);
        return CommonResponse.success();
    }

    @GetMapping("/getById/{id}")
    @ApiOperation("获取数据字典信息")
    public CommonResponse<SysDictVO> getById(@PathVariable Long id) {
        return CommonResponse.success(dictInfoService.findById(id));
    }

    @PostMapping("/getByGroupCode")
    @ApiOperation("通过字典分组获取字典信息")
    public CommonResponse<List<SysDictVO>> getByGroupCode(@RequestParam("groupCode") String groupCode){
        return CommonResponse.success(dictInfoService.findByGroupCode(groupCode));
    }

    @GetMapping("/getList")
    @ApiOperation("获取数据字典列表 无分页")
    public CommonResponse<List<SysDictVO>> getList() {
        return CommonResponse.success(dictInfoService.findByList());
    }

    @PostMapping("/getListByPage")
    @ApiOperation("分页获取数据字典列表")
    public CommonResponse<IPage<SysDictVO>> getListByPage(@RequestBody @Valid QureySysDictDTO dto) {
        return CommonResponse.success(dictInfoService.findByPage(dto));
    }
}
