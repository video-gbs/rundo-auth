package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 功能关联资源请求体
 * @author Miracle
 * @date 2023/6/12 17:04
 */
@Data
public class PostFuncAssociateResourceReq {

    /**
     * 功能id
     */
    @NotNull(message = "功能id不能为空")
    @Min(value = 1, message = "非法功能id")
    private Long funcId;

    /**
     * 资源组
     */
    @NotBlank(message = "资源组不能为空")
    @Size(max = 250, message = "资源组命名过长")
    private String resourceKey;

    /**
     * 需要校验的param
     */
    @Size(max = 250, message = "需要校验的param值过长")
    private String validateParam;

    /**
     * 是否启用多维校验
     */
    @Range(min = 0, max = 1, message = "非法多维校验值")
    private Integer enableMultiCheck;

}
