package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName VideoAreaVO
 * @Description 安防区域响应实体
 * @date 2023-02-01 周三 14:07
 */
@Data
@ApiModel(value = "VideoAreaVO对象", description = "安防区域响应实体")
public class VideoAreaVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    @ApiModelProperty("安防区域名称")
    private String areaName;

    @ApiModelProperty("安防区域全名")
    private String areaNames;

    @ApiModelProperty("直接上级")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long areaPid;

    @ApiModelProperty("描述信息")
    private String description;

    @ApiModelProperty("层级")
    private Integer level;

    @ApiModelProperty("区域排序")
    private Integer areaSort;
}
