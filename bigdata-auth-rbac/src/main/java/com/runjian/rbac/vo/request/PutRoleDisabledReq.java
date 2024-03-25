package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 *  修改角色禁用状态请求体
 * @author Miracle
 * @date 2023/6/12 17:39
 */
@Data
public class PutRoleDisabledReq {

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空")
    @Min(value = 1, message = "非法角色id")
    private Long roleId;

    /**
     * 是否禁用 0-否 1-是
     */
    @NotNull(message = "禁用状态不能为空")
    @Range(min = 0, max = 1, message = "非法禁用状态")
    private Integer disabled;
}
