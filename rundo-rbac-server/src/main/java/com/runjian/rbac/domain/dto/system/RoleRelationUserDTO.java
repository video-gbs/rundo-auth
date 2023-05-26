package com.runjian.rbac.domain.dto.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RoleRelationUserDTO
 * @Description 关联用户实体
 * @date 2023-02-22 周三 11:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleRelationUserDTO {

    @ApiModelProperty("角色编号ID")
    @NotNull
    private Long roleId;

    @ApiModelProperty("用户编号列表")
    @NotNull
    private List<Long> userIdList;
}
