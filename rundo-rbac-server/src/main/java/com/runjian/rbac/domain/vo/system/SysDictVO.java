package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysDictVO
 * @Description
 * @date 2023-01-30 周一 15:01
 */
@Data
public class SysDictVO {

    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;
    @ApiModelProperty("分组名称")
    private String groupName;

    @ApiModelProperty("分组编码")
    private String groupCode;

    @ApiModelProperty("字典项名称")
    private String itemName;

    @ApiModelProperty("字典值")
    private String itemValue;

    @ApiModelProperty("字典描述")
    private String itemDesc;
}
