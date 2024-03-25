package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 修改功能请求体
 * @author Miracle
 * @date 2023/6/12 16:47
 */
@Data
public class PutFuncReq {

    /**
     * 功能id
     */
    @NotNull(message = "功能id不能为空")
    @Min(value = 1, message = "非法功能id")
    private Long id;

    /**
     * 菜单id
     */
    @NotNull(message = "菜单id不能为空")
    @Min(value = 0, message = "非法菜单id")
    private Long menuId;

    /**
     * 服务名称
     */
    @NotBlank(message = "服务名称不能为空")
    @Size(max = 250, message = "服务名称过长")
    private String serviceName;

    /**
     * 功能名称
     */
    @NotBlank(message = "范围不能为空")
    @Size(max = 100, message = "范围过长")
    private String funcName;

    /**
     * 范围
     */
    @NotBlank(message = "功能名称不能为空")
    @Size(max = 250, message = "功能名称过长")
    private String scope;

    /**
     * 路径url
     */
    @NotBlank(message = "访问地址不能为空")
    @Size(max = 250, message = "访问地址过长")
    private String path;

    /**
     * 方法
     */
    @NotNull(message = "请求方法不能为空")
    @Range(min = 1, max = 8, message = "非法请求方法")
    private Integer method;

}
