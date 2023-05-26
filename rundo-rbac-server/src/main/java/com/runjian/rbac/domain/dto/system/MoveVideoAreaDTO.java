package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName MoveVideoAreaDTO
 * @Description 移动安防区域
 * @date 2023-02-01 周三 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "移动安防区域", description = "移动安防区域")
public class MoveVideoAreaDTO {

    @ApiModelProperty("主键ID")
    private Long id;
    @ApiModelProperty("直接上级")
    private Long areaPid;
}
