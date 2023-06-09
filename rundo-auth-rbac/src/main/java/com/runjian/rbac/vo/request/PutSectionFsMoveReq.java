package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 部门父子移动请求体
 * @author Miracle
 * @date 2023/6/9 14:38
 */
@Data
public class PutSectionFsMoveReq {

    /**
     * 部门id
     */
    @NotNull(message = "部门id不能为空")
    @Min(value = 1, message = "非法部门id")
    private Long id;

    /**
     * 部门父id
     */
    @NotNull(message = "部门父id不能为空")
    @Min(value = 1, message = "非法部门父id")
    private Long sectionPid;
}
