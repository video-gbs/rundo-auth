package com.runjian.rbac.controller.system;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONUtil;
import com.runjian.rbac.constant.AddGroup;
import com.runjian.rbac.constant.UpdateGroup;
import com.runjian.rbac.domain.dto.system.MoveVideoAreaDTO;
import com.runjian.rbac.domain.dto.system.VideoAreaDTO;
import com.runjian.rbac.domain.vo.system.VideoAreaVO;
import com.runjian.rbac.service.system.VideoAreaService;
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
 * 安防区域 前端控制器
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-03 11:53:37
 */
@Api(tags = "安防区域")
@Slf4j
@RestController
@RequestMapping("/videoArae")
public class VideoAreaController {

    @Autowired
    private VideoAreaService videoAreaService;

    @PostMapping("/add")
    @ApiOperation("添加安防区域")
    public CommonResponse<VideoAreaVO> save(@RequestBody @Validated({AddGroup.class}) VideoAreaDTO dto) {
        log.info("添加安防区域前端传参信息{}", JSONUtil.toJsonStr(dto));
        return CommonResponse.success(videoAreaService.save(dto));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation("删除安防区域")
    public CommonResponse<?> delete(@PathVariable Long id) {
        return videoAreaService.erasureById(id);
    }

    @PostMapping("/update")
    @ApiOperation("编辑安防区域")
    public CommonResponse<?> update(@RequestBody @Validated({UpdateGroup.class}) VideoAreaDTO dto) {
        videoAreaService.modifyById(dto);
        return CommonResponse.success();
    }

    @PostMapping("/move")
    @ApiOperation("移动安防区域")
    public CommonResponse<?> move(@RequestBody MoveVideoAreaDTO dto) {
        videoAreaService.moveVideoArea(dto);
        return CommonResponse.success();
    }


    @GetMapping("/getById/{id}")
    @ApiOperation("获取安防区域信息")
    public CommonResponse<VideoAreaVO> getById(@PathVariable Long id) {
        return CommonResponse.success(videoAreaService.findById(id));
    }

    @PostMapping("/getList")
    @ApiOperation("获取安防区域列表")
    public CommonResponse<List<VideoAreaVO>> getList(@RequestParam(value = "areaId", required = false) Long areaId) {
        return CommonResponse.success(videoAreaService.findByList(areaId));
    }

    @GetMapping("/tree")
    @ApiOperation("获取安防区域层级树")
    public CommonResponse<List<Tree<Long>>> getTreeList() {
        return CommonResponse.success(videoAreaService.findByTree());
    }

}
