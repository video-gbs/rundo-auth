package com.runjian.rbac.domain.vo.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName SysApiInfoVO
 * @Description 接口详情
 * @date 2023-01-30 周一 14:27
 */
@Data
public class SysApiInfoVO {
    @ApiModelProperty("主键ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long id;

    @ApiModelProperty("所属应用ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long appId;

    @ApiModelProperty("接口直接父ID")
    @JsonFormat(shape =JsonFormat.Shape.STRING )
    private Long apiPid;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("排序")
    private Integer apiSort;

    @ApiModelProperty("跳转链接")
    private String url;

    @ApiModelProperty("接口层级")
    private Integer level;
}
