package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

/**
 * 修改角色请求体
 * @author Miracle
 * @date 2023/6/12 17:43
 */
@Data
public class PutRoleReq {

    /**
     * 角色id
     */
    @NotNull(message = "角色Id不能为空")
    @Min(value = 1, message = "非法角色id")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 250, message = "角色名称过长")
    private String roleName;

    /**
     * 角色描述
     */
    @Size(max = 250, message = "角色描述过长")
    private String roleDesc;

    /**
     * 菜单id数组
     */
    @Size(max = 999999, message = "菜单id数组过长")
    private Set<Long> menuIds;

    /**
     * 功能id数组
     */
    @Size(max = 999999, message = "功能id数组过长")
    private Set<Long> funcIds;

    /**
     * 资源id数组
     */
    @Size(max = 999999, message = "资源id数组过长")
    private Set<Long> resourceIds;
}
