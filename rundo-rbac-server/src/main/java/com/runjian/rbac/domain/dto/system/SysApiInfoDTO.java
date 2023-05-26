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
 * @ClassName SysApiInfoDTO
 * @Description 添加接口
 * @date 2023-01-10 周二 10:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "接口信息", description = "接口信息")
public class SysApiInfoDTO {

    @ApiModelProperty("编号ID")
    @NotNull(groups = {UpdateGroup.class}, message = "id不能为空")
    private Long id;

    @ApiModelProperty("应用ID")
    @NotNull
    private Long appId;

    @ApiModelProperty("应用ID")
    @NotNull
    private String appName;

    @ApiModelProperty("接口直接父ID(即接口分组，菜单页面)")
    @NotNull
    private Long apiPid;

    @ApiModelProperty("接口名称")
    @NotNull
    private String apiName;

    @ApiModelProperty("跳转链接")
    private String url;

    @ApiModelProperty("接口排序")
    @NotNull
    private Integer apiSort;

    @ApiModelProperty(value = "禁用状态",notes = "0正常，1禁用")
    @NotNull
    private Integer status;
}
