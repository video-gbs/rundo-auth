package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 * 修改功能关联资源禁用状态请求体
 * @author Miracle
 * @date 2023/6/12 17:15
 */
@Data
public class PutFuncResourceDisabledReq {

    /**
     * 关联资源id
     */
    @NotNull(message = "关联资源id不能为空")
    @Min(value = 1, message = "非法关联资源id")
    private Long funcResourceId;

    /**
     * 是否禁用 0-否 1-是
     */
    @NotNull(message = "禁用状态不能为空")
    @Range(min = 0, max = 1, message = "非法禁用状态")
    private Integer disabled;
}
