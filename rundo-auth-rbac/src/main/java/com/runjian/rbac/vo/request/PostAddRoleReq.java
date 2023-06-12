package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 添加角色请求体
 * @author Miracle
 * @date 2023/6/12 17:29
 */
@Data
public class PostAddRoleReq {

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Max(value = 250, message = "角色名称过长")
    private String roleName;

    /**
     * 角色描述
     */
    @Max(value = 250, message = "角色描述过长")
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
