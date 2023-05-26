package com.runjian.rbac.domain.vo.system;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Jiang4Yu
 * @version V1.0.0
 * @ClassName HomeVO
 * @Description 首页VO对象
 * @date 2023-02-09 周四 0:59
 */
@Data
@ApiModel(value = "首页VO对象", description = "首页VO对象")
public class HomeVO {
    @ApiModelProperty("功能应用")
    private List<SysAppInfoVO> appList;

    @ApiModelProperty("配置应用")
    private List<SysAppInfoVO> configList;

    @ApiModelProperty("运维应用")
    private List<SysAppInfoVO> devOpsList;
}
