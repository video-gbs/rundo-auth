package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 修改用户禁用状态请求体
 * @author Miracle
 * @date 2023/6/9 15:33
 */
@Data
public class PutUserDisabledReq {

    /**
     * 用户Id
     */
    @NotNull(message = "用户Id不能为空")
    @Min(value = 1, message = "非法用户id")
    private Long userId;

    /**
     * 是否禁用 0-否 1-是
     */
    @NotNull(message = "禁用状态不能为空")
    @Range(min = 0, max = 1, message = "非法禁用状态")
    private Integer disabled;
}
