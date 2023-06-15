package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.DictService;
import com.runjian.rbac.vo.dto.AuthDataDto;
import com.runjian.rbac.vo.request.PostAddDictReq;
import com.runjian.rbac.vo.request.PutDictReq;
import com.runjian.rbac.vo.response.GetDictGroupRsp;
import com.runjian.rbac.vo.response.GetDictPageRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 字典接口
 * @author Miracle
 * @date 2023/6/8 9:44
 */
@Slf4j
@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
public class DictController {

    private final ValidatorService validatorService;

    private final DictService dictService;


    /**
     * 分页查询字典
     * @param page 页码
     * @param num 数量
     * @param groupName 分组名称
     * @param itemName 字典名称
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page")
    @ApiDoc(result = GetDictPageRsp.class)
    public CommonResponse<PageInfo<GetDictPageRsp>> getDictPage(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int num,
                                                                String groupName, String itemName){
        return CommonResponse.success(dictService.getDictPage(page, num, groupName, itemName));
    }

    /**
     * 根据分组编码获取字典
     * @param groupCode
     * @return
     */
    @GetMapping("/group")
    @ApiDoc(result = GetDictGroupRsp.class)
    public CommonResponse<List<GetDictGroupRsp>> getDictGroup(@RequestParam String groupCode){
        return CommonResponse.success(dictService.getDictByGroupName(groupCode));
    }

    /**
     * 增加字典
     * @param req 增加字典请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addDict(@RequestBody PostAddDictReq req){
        validatorService.validateRequest(req);
        dictService.addDict(req.getGroupName(), req.getGroupCode(), req.getItemName(), req.getItemValue(), req.getDescription());
        return CommonResponse.success();
    }

    /**
     * 修改字典
     * @param req 修改字典请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateDict(@RequestBody PutDictReq req){
        validatorService.validateRequest(req);
        dictService.updateDict(req.getDictId(), req.getGroupName(), req.getGroupCode(), req.getItemName(), req.getItemValue(), req.getDescription());
        return CommonResponse.success();
    }

    /**
     * 批量删除字典
     * @param dictIds 字典数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteDict(@RequestParam Set<Long> dictIds){
        dictService.batchDelete(dictIds);
        return CommonResponse.success();
    }
}
