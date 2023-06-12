package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 启禁用菜单请求体
 * @author Miracle
 * @date 2023/6/12 14:59
 */
@Data
public class PutMenuDisabledReq {

    /**
     * 菜单id
     */
    @NotNull(message = "菜单id不能为空")
    @Min(value = 1, message = "非法菜单id")
    private Long menuId;

    /**
     * 是否禁用 0-否 1-是
     */
    @NotNull(message = "禁用状态不能为空")
    @Range(min = 0, max = 1, message = "非法禁用状态")
    private Integer disabled;
}
