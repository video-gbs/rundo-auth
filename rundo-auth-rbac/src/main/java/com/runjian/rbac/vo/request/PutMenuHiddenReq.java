package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 菜单隐藏请求体
 * @author Miracle
 * @date 2023/6/12 15:04
 */
@Data
public class PutMenuHiddenReq {

    /**
     * 菜单id
     */
    @NotNull(message = "菜单Id不能为空")
    @Min(value = 1, message = "非法菜单id")
    private Long menuId;

    /**
     * 是否隐藏 0-否 1-是
     */
    @NotNull(message = "隐藏状态不能为空")
    @Range(min = 0, max = 1, message = "非法隐藏状态")
    private Integer hidden;
}
