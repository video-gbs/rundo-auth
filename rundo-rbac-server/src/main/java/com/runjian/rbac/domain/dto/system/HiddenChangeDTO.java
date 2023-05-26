package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HiddenChangeDTO
 * @Description 菜单隐藏状态
 * @date 2023-02-23 周四 11:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "菜单隐藏状态", description = "菜单隐藏状态")
public class HiddenChangeDTO {

    private Long id;

    private Integer hidden;
}
