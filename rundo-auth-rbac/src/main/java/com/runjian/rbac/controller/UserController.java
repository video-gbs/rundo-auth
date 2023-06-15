package com.runjian.rbac.controller;

import com.github.pagehelper.PageInfo;
import com.runjian.common.aspect.annotation.BlankStringValid;
import com.runjian.common.aspect.annotation.IllegalStringValid;
import com.runjian.common.config.response.CommonResponse;
import com.runjian.common.validator.ValidatorService;
import com.runjian.rbac.service.rbac.UserService;
import com.runjian.rbac.vo.request.PostAddUserReq;
import com.runjian.rbac.vo.request.PutUserDisabledReq;
import com.runjian.rbac.vo.request.PutUserReq;
import com.runjian.rbac.vo.response.GetUserPageRsp;
import com.runjian.rbac.vo.response.GetUserRsp;
import io.github.yedaxia.apidocs.ApiDoc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * 用户接口
 * @author Miracle
 * @date 2023/6/8 9:40
 */
@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final ValidatorService validatorService;

    private final UserService userService;



    /**
     * 角色页面获取绑定或未绑定的数据
     * @param page 页码
     * @param num 数量
     * @param roleId 角色id
     * @param username 用户名
     * @param isBinding 是否已绑定
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page/role")
    @ApiDoc(result = GetUserPageRsp.class)
    public CommonResponse<PageInfo<GetUserPageRsp>> getUserRolePage(@RequestParam(defaultValue = "1") int page,
                                                                   @RequestParam(defaultValue = "10") int num,
                                                                   @RequestParam Long roleId, String username, Boolean isBinding){
        return CommonResponse.success(userService.getUserPage(page, num, roleId, username, isBinding));
    }

    /**
     * 根据部门层级查询用户
     * @param page 页码
     * @param num 数量
     * @param sectionId 部门id
     * @param username 用户名
     * @param workName 工作名称
     * @param isInclude 是否包含子节点数据
     * @return
     */
    @BlankStringValid
    @IllegalStringValid
    @GetMapping("/page")
    @ApiDoc(result = GetUserPageRsp.class)
    public CommonResponse<PageInfo<GetUserPageRsp>> getUserPage(@RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "10") int num,
                                                                @RequestParam Long sectionId,
                                                                String username, String workName,
                                                                @RequestParam Boolean isInclude){

        return CommonResponse.success(userService.getUserPage(page, num, sectionId, username, workName, isInclude));
    }



    /**
     * 新增用户
     * @param req 新增用户请求体
     * @return
     */
    @PostMapping("/add")
    public CommonResponse<?> addUser(@RequestBody PostAddUserReq req){
        validatorService.validateRequest(req);
        userService.addUser(req.getUsername(), req.getPassword(), req.getSectionId(),
                req.getExpiryStartTime(), req.getExpiryEndTime(), req.getWorkName(),
                req.getWorkNum(), req.getAddress(), req.getPhone(), req.getDescription(),
                req.getRoleIds());
        return CommonResponse.success();
    }

    /**
     * 修改用户禁用状态
     * @param req 修改用户禁用状态请求体
     * @return
     */
    @PutMapping("/update/disabled")
    public CommonResponse<?> updateDisabled(@RequestBody PutUserDisabledReq req){
        validatorService.validateRequest(req);
        userService.updateDisabled(req.getUserId(), req.getDisabled());
        return CommonResponse.success();
    }

    /**
     * 修改用户信息
     * @param req 修改用户请求体
     * @return
     */
    @PutMapping("/update")
    public CommonResponse<?> update(@RequestBody PutUserReq req){
        validatorService.validateRequest(req);
        userService.updateUser(req.getUserId(), req.getExpiryEndTime(), req.getPassword(),
                req.getSectionId(), req.getWorkName(), req.getWorkNum(),
                req.getPhone(), req.getAddress(), req.getDescription(),
                req.getRoleIds());
        return CommonResponse.success();
    }

    /**
     * 批量删除用户
     * @param userIds 用户id数组
     * @return
     */
    @DeleteMapping("/delete")
    public CommonResponse<?> delete(@RequestParam Set<Long> userIds){
        userService.batchDeleteUser(userIds);
        return CommonResponse.success();
    }
}
