package com.runjian.rbac.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.request.*;
import com.runjian.rbac.vo.response.*;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 资源接口
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
     * @param resourceKey 资源组
     * @param isIncludeResource 是否包含资源数据
     * @return
     */
    @GetMapping("/tree")
    @ApiDoc(result = GetResourceTreeRsp.class)
    public CommonResponse<GetResourceTreeRsp> getResourcePage(@RequestParam String resourceKey, @RequestParam Boolean isIncludeResource){
        return CommonResponse.success(resourceService.getResourceTree(resourceKey, isIncludeResource));
    }

    /**
     * 获取根节点资源
     * @return
     */
    @GetMapping("/root")
    @ApiDoc(result = GetResourceRootRsp.class)
    public CommonResponse<List<GetResourceRootRsp>> getResourceRoot(){
        return CommonResponse.success(resourceService.getResourceRoot());
    }


    /**
     * 添加根节点
     * @param req 添加资源根节点请求体
     * @return
     */
    @PostMapping("/root/add")
    public CommonResponse<?> addRootResource(@RequestBody PostResourceRootReq req){
        validatorService.validateRequest(req);
        resourceService.addResourceRoot(req.getResourceKey(), req.getResourceName(), req.getResourceValue());
        return CommonResponse.success();
    }

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/batch/add")
    public CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceReq req){
        validatorService.validateRequest(req);
        resourceService.batchAddResource(req.getResourcePid(), req.getResourceType(), req.getResourceMap());
        return CommonResponse.success();
    }

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/batch/add/kv")
    public CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceKvReq req){
        validatorService.validateRequest(req);
        resourceService.batchAddResourceByKv(req.getResourceKey(), req.getParentResourceValue(), req.getResourceType(), req.getResourceMap());
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
     * 修改资源
     * @param req 修改资源请求体
     * @return
     */
    @PutMapping("/update/kv")
    public CommonResponse<?> updateResource(@RequestBody PutResourceKvReq req){
        validatorService.validateRequest(req);
        resourceService.updateResourceByKv(req.getResourceKey(), req.getResourceValue(), req.getResourceValue());
        return CommonResponse.success();
    }

    /**
     * 删除资源
     * @param resourceId 资源id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> delete(@RequestParam Long resourceId){
        resourceService.delete(resourceId);
        return CommonResponse.success();
    }

    /**
     * 删除
     * @param resourceKey 资源key
     * @param resourceValue 资源value
     * @return
     */
    @DeleteMapping("/delete/kv")
    public CommonResponse<?> deleteByResourceKeyAndResourceValue(@RequestParam String resourceKey, @RequestParam String resourceValue){
        resourceService.deleteByResourceByKv(resourceKey, resourceValue);
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
        resourceService.fsMove(req.getId(), req.getResourcePid());
        return CommonResponse.success();
    }

    /**
     * 资源父子级别移动
     * @param req 资源父子移动请求体
     * @return
     */
    @PutMapping("/move/fs/kv")
    public CommonResponse<?> fsMove(@RequestBody PutResourceFsMoveKvReq req){
        validatorService.validateRequest(req);
        resourceService.fsMoveByKv(req.getResourceKey(), req.getResourceValue(), req.getParentResourceValue());
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

    /**
     * 部门的兄弟节点移动
     * @param req 部门兄弟节点移动请求体
     * @return
     */
    @PutMapping("/move/bt/kv")
    public CommonResponse<?> btMove(@RequestBody PutResourceBtMoveKvReq req){
        validatorService.validateRequest(req);
        resourceService.btMoveByKv(req.getResourceKey(), req.getResourceValue(), req.getMoveOp());
        return CommonResponse.success();
    }
}
