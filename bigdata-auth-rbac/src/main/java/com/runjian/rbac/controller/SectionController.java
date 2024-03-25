package com.runjian.rbac.controller;

import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.SectionService;
import com.runjian.rbac.vo.request.PostAddSectionReq;
import com.runjian.rbac.vo.request.PutSectionBtMoveReq;
import com.runjian.rbac.vo.request.PutSectionFsMoveReq;
import com.runjian.rbac.vo.request.PutSectionReq;
import com.runjian.rbac.vo.response.GetSectionTreeRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 部门接口
 * @author Miracle
 * @date 2023/6/8 9:41
 */
@Slf4j
@RestController
@RequestMapping("/section")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    private final ValidatorService validatorService;

    /**
     * 获取部门树
     * @return
     */
    @GetMapping("/tree")
    @ApiDoc(result = GetSectionTreeRsp.class)
    public CommonResponse<GetSectionTreeRsp> getSectionTree(){
        return CommonResponse.success(sectionService.getSectionTree());
    }

    /**
     * 添加部门
     * @param req 部门添加请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<Long> addSection(@RequestBody PostAddSectionReq req){
        validatorService.validateRequest(req);
        return CommonResponse.success(sectionService.addSection(req.getSectionPid(), req.getSectionName(), req.getLeaderName(), req.getPhone(), req.getDescription()));
    }

    /**
     * 修改部门
     * @param req 部门修改请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> updateSection(@RequestBody PutSectionReq req){
        validatorService.validateRequest(req);
        sectionService.updateSection(req.getId(), req.getSectionName(), req.getLeaderName(), req.getPhone(), req.getDescription());
        return CommonResponse.success();
    }

    /**
     * 删除部门
     * @param sectionId 部门id
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> deleteSection(@RequestParam Long sectionId){
        sectionService.deleteSection(sectionId);
        return CommonResponse.success();
    }

    /**
     * 部门父子级别移动
     * @param req 部门父子移动请求体
     * @return
     */
    @PutMapping("/move/fs")
    public CommonResponse<?> fsMove(@RequestBody PutSectionFsMoveReq req){
        validatorService.validateRequest(req);
        sectionService.fsMove(req.getId(), req.getSectionPid());
        return CommonResponse.success();
    }

    /**
     * 部门的兄弟节点移动
     * @param req 部门兄弟节点移动请求体
     * @return
     */
    @PutMapping("/move/bt")
    public CommonResponse<?> btMove(@RequestBody PutSectionBtMoveReq req){
        validatorService.validateRequest(req);
        sectionService.btMove(req.getId(), req.getMoveOp());
        return CommonResponse.success();
    }

}
