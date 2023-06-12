package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 添加功能请求体
 * @author Miracle
 * @date 2023/6/12 16:32
 */
@Data
public class PostAddFuncReq {

    /**
     * 菜单id
     */
    @NotNull(message = "菜单id不能为空")
    @Min(value = 1, message = "非法菜单id")
    private Long menuId;

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    @Size(max = 250, message = "服务名称过长")
    private String serviceName;

    /**
     * 范围
     */
    @NotBlank(message = "范围不能为空")
    @Size(max = 100, message = "范围过长")
    private String scope;

    /**
     * 功能名称
     */
    @NotBlank(message = "功能名称不能为空")
    @Size(max = 250, message = "功能名称过长")
    private String funcName;

    /**
     * 访问地址
     */
    @NotBlank(message = "访问地址不能为空")
    @Size(max = 250, message = "访问地址过长")
    private String path;

    /**
     * 请求方法
     */
    @NotNull(message = "请求方法不能为空")
    @Range(min = 1, max = 8, message = "非法请求方法")
    private Integer method;

    /**
     * 是否禁用
     */
    @NotNull(message = "是否禁用不能为空")
    @Range(min = 0, max = 1, message = "非法禁用选项")
    private Integer disabled;
}
