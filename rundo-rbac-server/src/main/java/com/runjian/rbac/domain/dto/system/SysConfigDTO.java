package com.runjian.rbac.domain.dto.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AddSysConfigDTO
 * @Description 添加系统参数配置
 * @date 2023-01-30 周一 14:48
 */
@Data
@AllArgsConstructor
public class SysConfigDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty("参数名称")
    @TableField("param_name")
    private String paramName;

    @ApiModelProperty("参数编码")
    @TableField("param_code")
    private String paramCode;

    @ApiModelProperty("参数值")
    @TableField("param_value")
    private String paramValue;

    @ApiModelProperty("参数描述")
    @TableField("param_desc")
    private String paramDesc;
}
