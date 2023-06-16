package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.FuncService;
import com.runjian.rbac.vo.request.*;
import com.runjian.rbac.vo.response.GetDictGroupRsp;
import com.runjian.rbac.vo.response.GetFuncPageRsp;
import com.runjian.rbac.vo.response.GetFuncResourceRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能接口
 * @author Miracle
 * @date 2023/6/8 9:43
 */
@Slf4j
@RestController
@RequestMapping("/func")
@RequiredArgsConstructor
public class FuncController {

    private final FuncService funcService;

    private final ValidatorService validatorService;


    /**
     * 查询功能列表
     * @param page 页数
     * @param num 数量
     * @param menuId 菜单id
     * @param serviceName 服务名称
     * @param funcName 功能名称
     * @param isInclude 是否包含下级节点
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page")
    @ApiDoc(result = GetFuncPageRsp.class)
    public CommonResponse<PageInfo<GetFuncPageRsp>> getFuncPage(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int num,
                                                                @RequestParam Long menuId,
                                                                String serviceName, String funcName,
                                                                @RequestParam Boolean isInclude){
        return CommonResponse.success(funcService.getFuncPage(page, num, menuId, serviceName, funcName, isInclude));
    }

    /**
     * 添加功能
     * @param req 添加功能请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addFunc(@RequestBody PostAddFuncReq req){
        validatorService.validateRequest(req);
        funcService.addFunc(req.getMenuId(), req.getServiceName(), req.getFuncName(), req.getScope(), req.getPath(), req.getMethod());
        return CommonResponse.success();
    }

    /**
     * 修改功能禁用状态
     * @param req 修改功能禁用状态请求体
     * @return
     */
    @PutMapping("/update/disabled")
    public CommonResponse<?> updateDisabled(@RequestBody PutFuncDisabledReq req){
        validatorService.validateRequest(req);
        funcService.updateDisabled(req.getId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 修改功能
     * @param req 修改功能请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> update(@RequestBody PutFuncReq req){
        validatorService.validateRequest(req);
        funcService.updateFunc(req.getId(), req.getMenuId(),
                req.getServiceName(), req.getFuncName(),
                req.getScope(), req.getPath(),
                req.getMethod());
        return CommonResponse.success();
    }

    /**
     * 删除功能
     * @param id 功能id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> delete(@RequestParam Long id){
        funcService.deleteFunc(id);
        return CommonResponse.success();
    }

    /**
     * 获取功能资源信息
     * @param funcId
     * @return
     */
    @GetMapping("/resource/list")
    public CommonResponse<List<GetFuncResourceRsp>> getFuncResource(@RequestParam Long funcId){
        return CommonResponse.success(funcService.getFuncResource(funcId));
    }

    /**
     * 功能关联资源
     * @param req 功能关联资源请求体
     * @return
     */
    @PostMapping("/resource/associate")
    public CommonResponse<?> associateResource(@RequestBody PostFuncAssociateResourceReq req){
        validatorService.validateRequest(req);
        funcService.associateResource(req.getFuncId(), req.getResourceKey(), req.getValidateParam());
        return CommonResponse.success();
    }

    /**
     * 修改关联资源禁用状态
     * @param req 修改关联资源禁用状态请求体
     * @return
     */
    @PutMapping("/resource/update/disabled")
    public CommonResponse<?> updateFuncResourceDisabled(@RequestBody PutFuncResourceDisabledReq req){
        validatorService.validateRequest(req);
        funcService.updateFuncResourceDisabled(req.getFuncResourceId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 修改功能关系资源信息
     * @param req 修改功能关系资源请求体
     * @return
     */
    @PutMapping("/resource/update")
    public CommonResponse<?> updateFuncResource(@RequestBody PutFuncResourceReq req){
        validatorService.validateRequest(req);
        funcService.updateFuncResource(req.getFuncResourceId(), req.getResourceKey(), req.getValidateParam());
        return CommonResponse.success();
    }

    /**
     * 资源删除
     * @param funcResourceId 资源id
     * @return
     */
    @DeleteMapping("/resource/delete")
    public CommonResponse<?> deleteFuncResource(@RequestBody Long funcResourceId){
        funcService.deleteFuncResource(funcResourceId);
        return CommonResponse.success();
    }
}
