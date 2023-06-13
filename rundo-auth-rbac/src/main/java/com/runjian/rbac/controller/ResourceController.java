package com.runjian.rbac.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.request.*;
import com.runjian.rbac.vo.response.GetResourceTreeRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 资源
 * @author Miracle
 * @date 2023/6/8 9:43
 */
@Slf4j
@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
public class ResourceController {

    private final ResourceService resourceService;

    private final ValidatorService validatorService;

    /**
     * 分页查询资源
     * @param page 页数
     * @param num 数量
     * @param name 资源名字
     * @param resourceKey 资源组
     * @return
     */
    @GetMapping("/tree")
    public CommonResponse<GetResourceTreeRsp> getResourcePage(@RequestParam String resourceKey, @RequestParam Boolean isIncludeResource){
        return CommonResponse.success(resourceService.getResourceTree(resourceKey, isIncludeResource));
    }

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/batch/add")
    public CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceReq req){
        validatorService.validateRequest(req);
        resourceService.batchAddResource(req.getResourcePid(), req.getResourceType(), req.getResourceName(), req.getResourceKey(), req.getResourceValue());
        return CommonResponse.success();
    }

    /**
     * 修改资源
     * @param req 修改资源请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateResource(@RequestBody PutResourceReq req){
        validatorService.validateRequest(req);
        resourceService.updateResource(req.getResourceId(), req.getResourceName(), req.getResourceValue());
        return CommonResponse.success();
    }

    /**
     * 批量删除资源
     * @param resourceIds 资源id数组
     * @return
     */
    @DeleteMapping("/delete/batch")
    public CommonResponse<?> deleteResource(@RequestParam Set<Long> resourceIds){
        resourceService.batchDelete(resourceIds);
        return CommonResponse.success();
    }

    /**
     * 资源父子级别移动
     * @param req 资源父子移动请求体
     * @return
     */
    @PutMapping("/move/fs")
    public CommonResponse<?> fsMove(@RequestBody PutResourceFsMoveReq req){
        validatorService.validateRequest(req);
        resourceService.fsMove(req.getId(), req.getSectionPid());
        return CommonResponse.success();
    }

    /**
     * 部门的兄弟节点移动
     * @param req 部门兄弟节点移动请求体
     * @return
     */
    @PutMapping("/move/bt")
    public CommonResponse<?> btMove(@RequestBody PutResourceBtMoveReq req){
        validatorService.validateRequest(req);
        resourceService.btMove(req.getId(), req.getMoveOp());
        return CommonResponse.success();
    }
}
