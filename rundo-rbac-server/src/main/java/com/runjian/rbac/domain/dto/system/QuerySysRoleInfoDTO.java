package com.runjian.rbac.domain.dto.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.runjian.rbac.domain.dto.common.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName QuerySysRoleInfoDTO
 * @Description 角色分页查询
 * @date 2023-02-02 周四 9:41
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "角色分页查询", description = "角色信息查询条件")
public class QuerySysRoleInfoDTO extends CommonPage {
    @ApiModelProperty(value = "角色名称", required = true)
    private String roleName;

    @ApiModelProperty("创建人ID")
    private Long createdBy;

    @ApiModelProperty("创建人账户")
    private String userAccount;

    @ApiModelProperty("创建开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimeStart;

    @ApiModelProperty("创建时间结束")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTimeEnd;
}
