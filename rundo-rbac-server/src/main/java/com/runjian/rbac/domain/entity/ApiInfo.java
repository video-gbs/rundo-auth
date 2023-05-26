package com.runjian.rbac.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 接口信息表
 * </p>
 *
 * @author Jiang4Yu@126.com
 * @since 2023-01-04 15:16:33
 */
@Data
@EqualsAndHashCode
@TableName("api_info")
@ApiModel(value = "ApiInfo对象", description = "接口信息表")
public class ApiInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    @TableId("id")
    private Long id;

    @ApiModelProperty("所属应用")
    @TableField("app_id")
    private Long appId;

    @ApiModelProperty("所属应用")
    @TableField("app_name")
    private String appName;

    @ApiModelProperty("接口直接父ID")
    @TableField("api_pid")
    private Long apiPid;

    @ApiModelProperty("接口间接父ID")
    @TableField("api_pids")
    private String apiPids;

    @ApiModelProperty("接口名称")
    @TableField("api_name")
    private String apiName;

    @ApiModelProperty("接口排序")
    @TableField("api_sort")
    private Integer apiSort;

    @ApiModelProperty("接口层级")
    @TableField("level")
    private Integer level;

    @ApiModelProperty("跳转链接")
    @TableField("url")
    private String url;

    @ApiModelProperty("是否叶子节点")
    @TableField("leaf")
    private Integer leaf;

    @ApiModelProperty("禁用状态")
    @TableField("status")
    private Integer status;

    @ApiModelProperty("逻辑删除")
    @TableField(value = "delete_flag", fill = FieldFill.INSERT)
    @TableLogic
    private Integer deleteFlag;

    @ApiModelProperty("创建人")
    @TableField(value = "created_by", fill = FieldFill.INSERT)
    private Long createdBy;

    @ApiModelProperty("更新人")
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private Long updatedBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;


}
