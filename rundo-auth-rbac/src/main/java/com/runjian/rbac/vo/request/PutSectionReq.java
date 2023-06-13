package com.runjian.rbac.vo.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 部门修改请求体
 * @author Miracle
 * @date 2023/6/13 10:50
 */
@Data
public class PutSectionReq {

    /**
     * 部门父节点
     */
    @NotNull(message = "部门id不能为空")
    @Min(value = 0, message = "非法部门id")
    private Long id;

    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    @Size(max = 32, message = "部门名称,最长为32字符")
    private String sectionName;

    /**
     * 领导名称
     */
    @Size(max = 32, message = "领导名称,最长为32字符")
    private String leaderName;

    /**
     * 电话
     */
    @Size(max = 20, message = "电话,最长为20字符")
    private String phone;

    /**
     * 描述
     */
    @Size(max = 255, message = "描述,最长为255字符")
    private String description;
}
