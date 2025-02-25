package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 角色关联用户请求体
 * @author Miracle
 * @date 2023/6/12 17:48
 */
@Data
public class PostRoleUserAssociateReq {

    /**
     * 角色id
     */
    @NotNull(message = "角色Id不能为空")
    @Min(value = 1, message = "非法角色id")
    private Long roleId;

    /**
     * 用户id数组
     */
    @NotNull(message = "用户id数组不能为空")
    @Size(min = 1, max = 999999, message = "用户id数组过长")
    private Set<Long> userIds;

    /**
     * 添加指令
     */
    @NotNull(message = "是否添加操作不能为空")
    private Boolean isAdd;


}
