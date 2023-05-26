package com.runjian.rbac.domain.vo.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName RoleDetailVO
 * @Description 编辑时角色详情回显
 * @date 2023-02-09 周四 1:51
 */
@Data
@ApiModel(value = "编辑前角色信息", description = "编辑前角色信息")
public class RoleDetailVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty("角色名称")
    @TableField("role_name")
    private String roleName;

    @ApiModelProperty("角色描述")
    @TableField("role_desc")
    private String roleDesc;

    @ApiModelProperty("功能应用")
    private List<String> appIds;

    @ApiModelProperty("配置应用")
    private List<String> configIds;

    @ApiModelProperty("运维应用")
    private List<String> devopsIds;

    @ApiModelProperty("部门权限")
    private List<String> orgIds;

    @ApiModelProperty("安防区域")
    private List<String> areaIds;

    // @ApiModelProperty("视频通道ID列表")
    // List<Long> channelIds;
    // @ApiModelProperty("通道操作ID列表")
    // List<Long> operationIds;
}
