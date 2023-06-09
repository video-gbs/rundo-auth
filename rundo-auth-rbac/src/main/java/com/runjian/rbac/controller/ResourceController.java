package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.ResourceService;
import com.runjian.rbac.vo.request.PostBatchResourceReq;
import com.runjian.rbac.vo.request.PutResourceReq;
import com.runjian.rbac.vo.response.GetResourcePageRsp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
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
    @GetMapping("/page")
    public CommonResponse<PageInfo<GetResourcePageRsp>> getResourcePage(@RequestParam(defaultValue = "1") int page,
                                                                        @RequestParam(defaultValue = "10") int num,
                                                                        String name, String resourceKey){
        return CommonResponse.success(resourceService.getResourcePage(page, num, name, resourceKey));
    }

    /**
     * 批量添加资源
     * @param req 批量添加资源请求体
     * @return
     */
    @PostMapping("/batch/add")
    public CommonResponse<?> batchAddResource(@RequestBody PostBatchResourceReq req){
        validatorService.validateRequest(req);
        resourceService.batchAddResource(req.getGroupName(), req.getResourceName(), req.getResourceKey(), req.getResourceValue());
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
        resourceService.updateResource(req.getResourceId(), req.getGroupName(), req.getResourceName(), req.getResourceKey(), req.getResourceValue());
        return CommonResponse.success();
    }

    /**
     * 批量删除资源
     * @param resourceIds 资源id数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteResource(@RequestParam Set<Long> resourceIds){
        resourceService.batchDelete(resourceIds);
        return CommonResponse.success();
    }
}
