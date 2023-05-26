package com.runjian.rbac.domain.dto.system;

import com.runjian.rbac.constant.UpdateGroup;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName AddSysDictDTO
 * @Description 添加数字字典
 * @date 2023-01-30 周一 14:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "数字字典", description = "数字字典")
public class SysDictDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty("分组名称")
    @NotNull
    private String groupName;

    @ApiModelProperty("分组编码")
    @NotNull
    private String groupCode;

    @ApiModelProperty("字典项名称")
    @NotNull
    private String itemName;

    @ApiModelProperty("字典值")
    @NotNull
    private String itemValue;

    @ApiModelProperty("字典描述")
    private String itemDesc;
}
